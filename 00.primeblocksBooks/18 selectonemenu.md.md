# 19 selectOneMenu

```java
 <div class="field col-12 md:col-4">
                <p:outputLabel for="@next" value="Advanced"/>
                <p:selectOneMenu id="advanced" value="#{selectOneMenuView.country}" converter="#{countryConverter}"
                                panelStyle="width:180px" effect="fade" var="c"
                                filter="true" filterMatchMode="startsWith">

                    <f:selectItems value="#{selectOneMenuView.countries}" var="country"
                                itemLabel="#{country.name}" itemValue="#{country}"/>

                    <p:column style="width:10%">
                        <span class="flag flag-#{c.code}" style="width: 30px; height: 20px"/>
                    </p:column>

                    <p:column>
                        <f:facet name="header">
                            <h:outputText value="Name"/>
                        </f:facet>
                        <h:outputText value="#{c.name}"/>
                    </p:column>

                    <f:facet name="footer">
                        <h:outputText value="#{selectOneMenuView.countries.size()} countries"
                                    style="font-weight:bold;" styleClass="py-1 block"/>
                    </f:facet>
                </p:selectOneMenu>
            </div>

```