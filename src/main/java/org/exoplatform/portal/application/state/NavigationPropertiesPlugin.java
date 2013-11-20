/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.portal.application.state;

import java.util.Map;

import javax.xml.namespace.QName;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.portal.webui.application.UIPortlet;
import org.exoplatform.portal.webui.util.Util;

/**
 * This plugin adds a property that portlets can consume as 
 * public render parameter and which tells if the navigation
 * has changed by comparing the referer to the current URL.
 *
 * @author <a href="mailto:m.weiler@redhat.com">Martin Weiler</a>
 * @version $Revision$
 */
public class NavigationPropertiesPlugin extends AbstractContextualPropertyProviderPlugin {

    /** . */
    private final QName navigationChangedQName;

    public NavigationPropertiesPlugin(InitParams params) {
        super(params);

        //
        this.navigationChangedQName = new QName(namespaceURI, "navigation_changed");
        
        log.info("Initialized plugin, navigation changes are emitted as public render parameter with QName " + navigationChangedQName);
    }

    @Override
    public void getProperties(UIPortlet portletWindow, Map<QName, String[]> properties) {
        String hasChanged = "true";
        String referer = Util.getPortalRequestContext().getRequest().getHeader("referer");
        if(referer!=null) {
            if(referer.indexOf("?")>0)
                referer = referer.substring(0, referer.indexOf("?"));
            String currentUrl  = Util.getPortalRequestContext().getRequest().getRequestURL().toString();
            boolean isEqual = currentUrl.equals(referer);
            if(log.isDebugEnabled()) {
                log.debug("Compared navigation URLs. Referer: " + referer + ", Current URL: " + currentUrl + " => result: " + isEqual);
            }
            hasChanged = Boolean.toString(!isEqual);
        }
        addProperty(properties, navigationChangedQName, hasChanged);
    }
}
