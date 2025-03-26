# OutputLabel Texto Largo



## Forma 2:
Contar los caracteres y definir la cantidad de lineas.

```
 <p:column class="columnInputTextArea43" headerText="#{msg['field.tarea']}" >


                            <p:cellEditor >
                                <f:facet name="output">
                                  <p:inputTextarea  rows="#{item.tarea.length() le 70?1:(item.tarea.length()/70)+1}" style="width: 100% !important; float:left;text-decoration: line-through" autoResize="false" readonly="true" value="#{item.tarea}"  rendered="#{item.completado eq true}"/>
                                  <p:inputTextarea  rows="#{item.tarea.length() le 70?1:(item.tarea.length()/70)+1}" style="width: 100% !important;" autoResize="false" readonly="true" value="#{item.tarea}" rendered="#{item.completado eq false}" />

                                </f:facet>
                                <f:facet name="input" >
                                     <p:inputTextarea  rows="#{item.tarea.length() le 70?1:(item.tarea.length()/70)+1}" style="width: 100% !important;" autoResize="false"  value="#{item.tarea}"  />
                                </f:facet>
                            </p:cellEditor>

                        </p:column>

```