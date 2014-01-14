/*
 * � Copyright IBM Corp. 2010, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.xsp.bootstrap.renderkit.html_extended.extlib.outline.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xsp.bootstrap.util.BootstrapUtil;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.component.outline.AbstractOutline;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.AbstractTreeRenderer;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlListRenderer;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;

public class BootstrapAccordionRenderer extends AbstractTreeRenderer {
    
    private static final long serialVersionUID = 1L;

    private String accordionClientId;
    private String accordionInnerId;
    
    public BootstrapAccordionRenderer(String accordionClientId) {
        this.accordionClientId = accordionClientId;
        if(StringUtil.isNotEmpty(accordionInnerId)) {
        	this.accordionInnerId = StringUtil.replace(this.accordionClientId, ':', '_');
        } else {
        	this.accordionInnerId = BootstrapUtil.generateTemporaryUniqueId();
        }
    }
    
    public String getClientId() {
        return accordionClientId;
    }
    
    public String getInnerId() {
        return accordionInnerId;
    }
    
    @Override
    protected void preRenderTree(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        AbstractOutline outline = (AbstractOutline)tree.getComponent();
        
        writer.startElement("div", null); // $NON-NLS-1$
        String id = getClientId();
        if(StringUtil.isNotEmpty(id)) {
            writer.writeAttribute("id",id,null); // $NON-NLS-1$
        }
        String style = outline.getStyle();
        if(StringUtil.isNotEmpty(style)) {
            writer.writeAttribute("style",style,null); // $NON-NLS-1$
        }
        String styleClass = outline.getStyleClass();
        if(StringUtil.isNotEmpty(styleClass)) {
            writer.writeAttribute("class",styleClass,null); // $NON-NLS-1$
        }

        writer.startElement("div", null); // $NON-NLS-1$
        writer.writeAttribute("id",getInnerId(),null); // $NON-NLS-1$
        writer.writeAttribute("class","accordion",null); // $NON-NLS-1$
        
        writer.write('\n');
    }

    @Override
    protected void postRenderTree(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        writer.endElement("div"); // accordion //$NON-NLS-1$
        writer.endElement("div"); // main // $NON-NLS-1$
        writer.write('\n');
    }

    @Override
    protected void preRenderList(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
    	// Don't need to perform any output here
    }

    @Override
    protected void postRenderList(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
    	// Don't need to perform any output here
    }

    @Override
    protected void renderNode(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        // Generate a separator
        int type = tree.getNode().getType();
        if(type==ITreeNode.NODE_SEPARATOR) {
            // Not supported in a accordion
            return;
        }

        // JSF inserts a ':' that breaks CSS queries and thus Bootstrap
        int idx = tree.getNodeContext().getIndexInParent();
        String bodyId = getInnerId()+'_'+idx;
        
        // Generate a regular node
        String label = tree.getNode().getLabel();
        String image = tree.getNode().getImage();
        boolean enabled = tree.getNode().isEnabled();
        String style = getItemStyle(tree,enabled,false);
        String styleClass = getItemStyleClass(tree,enabled,false);

        boolean leaf = tree.getNode().getType()==ITreeNode.NODE_LEAF;
        String href = null;
        String onclick = null;
        if(leaf) {
            href = tree.getNode().getHref();
            onclick = findNodeOnClick(tree);
        }
        
        boolean hasLink = leaf && enabled && (StringUtil.isNotEmpty(onclick) || StringUtil.isNotEmpty(href));
        boolean hasImage = StringUtil.isNotEmpty(image);
        
        writer.startElement("div", null); // $NON-NLS-1$

        if(StringUtil.isNotEmpty(style)) {
            writer.writeAttribute("style",style,null); // $NON-NLS-1$
        }
        styleClass = ExtLibUtil.concatStyleClasses("accordion-group",styleClass);
        writer.writeAttribute("class",styleClass,null); // $NON-NLS-1$
        
        if(hasLink) {
            if (StringUtil.isNotEmpty(onclick)) {
            	// What to do here?
                //attrs.put("onClick", onclick); // $NON-NLS-1$ $NON-NLS-2$
            }           
        }

        writer.startElement("div", null); // $NON-NLS-1$
        writer.writeAttribute("class","accordion-heading",null); // $NON-NLS-1$

        writer.startElement("a", null); // $NON-NLS-1$
        writer.writeAttribute("class","accordion-toggle",null); // $NON-NLS-1$
        writer.writeAttribute("data-toggle","collapse",null); // $NON-NLS-1$
        writer.writeAttribute("data-parent","#"+getInnerId(),null); // $NON-NLS-1$
        writer.writeAttribute("href","#"+bodyId,null); // $NON-NLS-1$

		if(hasImage) {
			writer.startElement("img", null);
			if(StringUtil.isNotEmpty(image)) {
				image=HtmlRendererUtil.getImageURL(context, image);
				writer.writeAttribute("src", image, null);
			}
			writer.endElement("img");
		}
        
        if(StringUtil.isNotEmpty(label)) {
            writer.writeText(label, null); // $NON-NLS-1$
        }

        writer.endElement("a"); // $NON-NLS-1$
        writer.write('\n');
        writer.endElement("div"); // accordion-heading // $NON-NLS-1$
        writer.write('\n');
        
        writer.startElement("div", null); // $NON-NLS-1$
        writer.writeAttribute("id",bodyId,null); // $NON-NLS-1$
        String bodyClass = "accordion-body collapse";
        if(tree.getNode().isSelected()) {
        	bodyClass = ExtLibUtil.concatStyleClasses(bodyClass,"in");
        }
        writer.writeAttribute("class",bodyClass,null); // $NON-NLS-1$
        
        writer.startElement("div", null); // $NON-NLS-1$
        writer.writeAttribute("class","accordion-inner",null); // $NON-NLS-1$

        renderChildren(context, writer, tree);
        
        writer.endElement("div"); // accordion-body // $NON-NLS-1$
        writer.endElement("div"); // accordion-inner // $NON-NLS-1$
        writer.write('\n');

        writer.endElement("div"); // accordion-group // $NON-NLS-1$
        writer.write('\n');
    }

    @Override
    protected AbstractTreeRenderer getChildrenRenderer(TreeContextImpl tree) {
        if(tree.getDepth()==1) {
            return this;
        }
//        UIComponent c = tree.getComponent();
        return new HtmlListRenderer();
    }
}