This project shows how to extend the AbstractContextualPropertyProviderPlugin class of GateIn, with the intent to provide an easy mechanism for portlets to verify if navigation is coming from within the same portlet, or from outside. Note that due to the package visibility of some methods and members of the AbstractContextualPropertyProviderPlugin class, the custom extension needs to reside in the 'org.exoplatform.portal.application.state' package.

Build instructions:
==================

1. Download JPP 6.1 quickstarts and maven repository and follow the instructions to set up the maven repository

2. Run the build with
   $ mvn clean install -s /path/to/jpp610/quickstarts/settings-hosted-repo.xml
   

Installation instructions
=========================


1. Copy target/navigation-properties-plugin-<VERSION>.jar to `$JPP_HOME/modules/system/layers/gatein/org/gatein/lib/main`

2. Edit *$JPP_HOME/modules/system/layers/gatein/org/gatein/lib/main/module.xml*, and add the following to the `<resources>` element:

```xml   
    <resource-root path="navigation-properties-plugin-<VERSION>.jar"/>
```

3. Edit *$JPP_HOME/gatein/gatein.ear/portal.war/WEB-INF/conf/portal/web-configuration*, and apply the following change:   

```xml
  <component>
    <key>org.exoplatform.portal.application.state.ContextualPropertyManager</key>
    <type>org.exoplatform.portal.application.state.ContextualPropertyManagerImpl</type>
    <component-plugins>
      <!-- existing component-plugin elements ... -->
      <component-plugin>
        <name>NavigationPropertiesPlugin</name>
        <set-method>addPlugin</set-method>
        <type>org.exoplatform.portal.application.state.NavigationPropertiesPlugin</type>
        <init-params>
          <value-param>
            <name>namespaceURI</name>
            <description>Namespace URI</description>
            <value>http://www.gatein.org/xml/ns/navigation_prp_1_0</value>
          </value-param>
        </init-params>
      </component-plugin>
```

4. Portlets wanting to consume the added public render parameter need to add the following to  *PORTLET_APP/WEB-INF/portlet.xml*:

```xml
  <portlet>
    <portlet-name>JSPHelloUserPortlet</portlet-name>
    <portlet-class>org.jboss.portal.portlet.samples.JSPHelloUserPortlet</portlet-class>
    
    <!-- Portlet wants to consume the public render parameter -->
    <supported-public-render-parameter>navigation_changed</supported-public-render-parameter>
  </portlet>
  
  <public-render-parameter>
    <identifier>navigation_changed</identifier>
    <qname xmlns:prp='http://www.gatein.org/xml/ns/navigation_prp_1_0'>prp:navigation_changed</qname>
  </public-render-parameter>    
</portlet-app>
```
