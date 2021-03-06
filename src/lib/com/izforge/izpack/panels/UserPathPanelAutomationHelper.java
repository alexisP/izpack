/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 * 
 * http://izpack.org/
 * http://izpack.codehaus.org/
 * 
 * Copyright 2003 Jonathan Halliday
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.izforge.izpack.panels;

import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.installer.PanelAutomation;
import com.izforge.izpack.util.VariableSubstitutor;
import net.n3.nanoxml.XMLElement;

/**
 * Functions to support automated usage of the UserPathPanel
 *
 * @author Jonathan Halliday
 * @author Julien Ponge
 * @author Jeff Gordon
 */
public class UserPathPanelAutomationHelper implements PanelAutomation
{

    /**
     * Asks to make the XML panel data.
     *
     * @param idata     The installation data.
     * @param panelRoot The tree to put the data in.
     */
    public void makeXMLData(AutomatedInstallData idata, XMLElement panelRoot)
    {
        // Installation path markup
        XMLElement ipath = new XMLElement(UserPathPanel.pathElementName);
        // check this writes even if value is the default,
        // because without the constructor, default does not get set.
        ipath.setContent(idata.getVariable(UserPathPanel.pathVariableName));

        // Checkings to fix bug #1864
        XMLElement prev = panelRoot.getFirstChildNamed(UserPathPanel.pathElementName);
        if (prev != null)
        {
            panelRoot.removeChild(prev);
        }
        panelRoot.addChild(ipath);
    }

    /**
     * Asks to run in the automated mode.
     *
     * @param idata     The installation data.
     * @param panelRoot The XML tree to read the data from.
     * @return always true.
     */
    public boolean runAutomated(AutomatedInstallData idata, XMLElement panelRoot)
    {
        // We set the installation path
        XMLElement ipath = panelRoot.getFirstChildNamed(UserPathPanel.pathElementName);

        // Allow for variable substitution of the installpath value
        VariableSubstitutor vs = new VariableSubstitutor(idata.getVariables());
        String path = ipath.getContent();
        path = vs.substitute(path, null);
        idata.setVariable(UserPathPanel.pathVariableName, path);
        return true;
    }
}
