#-----------------------------------------------------------
# inputTextArea 
#-----------------------------------------------------------

```
 <p:column class="columnComentario43" headerText="#{msg['field.comentario']}">
    <p:cellEditor>
        <f:facet name="output"> 

            <p:inputTextarea  rows="2" style="width: 100% !important;" autoResize="false" readonly="true" value="#{item.comentario}"  />
        </f:facet>
        <f:facet name="input">
            <p:inputTextarea  rows="2" style="width: 100% !important;" autoResize="false"  value="#{item.comentario}"  />

        </f:facet>
    </p:cellEditor>
</p:column>

```