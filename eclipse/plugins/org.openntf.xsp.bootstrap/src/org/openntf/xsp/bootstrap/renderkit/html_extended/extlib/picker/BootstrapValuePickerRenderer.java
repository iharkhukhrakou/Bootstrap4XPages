/*
 * � Copyright IBM Corp. 2010
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

package org.openntf.xsp.bootstrap.renderkit.html_extended.extlib.picker;

import javax.faces.context.FacesContext;

import org.openntf.xsp.bootstrap.resources.BootstrapResources;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.component.picker.AbstractPicker;
import com.ibm.xsp.extlib.component.picker.data.IPickerData;
import com.ibm.xsp.extlib.renderkit.html_extended.picker.ValuePickerRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;

/**
 * Bootstrap value Picker renderer.
 */
public class BootstrapValuePickerRenderer extends ValuePickerRenderer {

	@Override
	protected String getDefaultDojoType() {
		return "extlib.dijit.BootstrapPickerList"; // $NON-NLS-1$
	}

	@Override
	protected String encodeDojoType(String dojoType) {
		// Transform the basic controls into Bootstrap ones
		if(StringUtil.equals(dojoType, "extlib.dijit.PickerCheckbox")) { // $NON-NLS-1$
			return "extlib.dijit.BootstrapPickerCheckbox"; // $NON-NLS-1$
		}
		if(StringUtil.equals(dojoType, "extlib.dijit.PickerList")) { // $NON-NLS-1$
			return "extlib.dijit.BootstrapPickerList"; // $NON-NLS-1$
		}
		if(StringUtil.equals(dojoType, "extlib.dijit.PickerListSearch")) { // $NON-NLS-1$
			return "extlib.dijit.BootstrapPickerListSearch"; // $NON-NLS-1$
		}
		return super.encodeDojoType(dojoType);
	}

	@Override
	protected void encodeExtraResources(FacesContext context, AbstractPicker picker, IPickerData data, UIViewRootEx rootEx, String dojoType) {
		if(StringUtil.equals(dojoType, "extlib.dijit.BootstrapPickerCheckbox")) { // $NON-NLS-1$
			ExtLibResources.addEncodeResource(rootEx, BootstrapResources.bootstrapPickerCheckbox);
		}
		if(StringUtil.equals(dojoType, "extlib.dijit.BootstrapPickerList")) { // $NON-NLS-1$
			ExtLibResources.addEncodeResource(rootEx, BootstrapResources.bootstrapPickerList);
		}
		if(StringUtil.equals(dojoType, "extlib.dijit.BootstrapPickerListSearch")) { // $NON-NLS-1$
			ExtLibResources.addEncodeResource(rootEx, BootstrapResources.bootstrapPickerListSearch);
		}
		super.encodeExtraResources(context, picker, data, rootEx, dojoType);
	}
}