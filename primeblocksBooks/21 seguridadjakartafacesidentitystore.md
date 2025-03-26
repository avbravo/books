
#21 seguridadjakartafacesidentitystore.md
- Configuer en el web.xml

```xml
<!--
    
        Seguridad    
    -->
    
    
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Developers pages</web-resource-name>
            <url-pattern>/application/*</url-pattern>
            
        </web-resource-collection>
        <auth-constraint>
            <role-name>DEVELOPERS</role-name>
            
        </auth-constraint>
    </security-constraint>
    
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Autoridad pages</web-resource-name>
            <url-pattern>/autoridad/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>DIRECTOR</role-name>
            <role-name>SUBDIRECTOR-ADMINISTRATIVO</role-name>
            <role-name>DEVELOPERS</role-name>
        </auth-constraint>
    </security-constraint>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Colaborador Pages</web-resource-name>
            <url-pattern>/colaborador/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>COLABORADOR</role-name>
            <role-name>DEVELOPERS</role-name>
        </auth-constraint>
    </security-constraint>
    
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>JefeUnidad Pages</web-resource-name>
            <url-pattern>/jefeunidad/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>JEFE-UNIDAD</role-name>
            <role-name>DEVELOPERS</role-name>
        </auth-constraint>
    </security-constraint>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Public pages</web-resource-name>
            <url-pattern>/login.xhtml</url-pattern>
            <url-pattern>/form.xhtml</url-pattern>
            <url-pattern>/index.xhtml</url-pattern>
        </web-resource-collection>
    </security-constraint>

    <security-role>
        <role-name>COLABORADOR</role-name>
    </security-role>
    <security-role>
        <role-name>JEFE-UNIDAD</role-name>
    </security-role>
    <security-role>
        <role-name>SUBDIRECTOR-ADMINISTRATIVO</role-name>
    </security-role>
    <security-role>
        <role-name>DIRECTOR</role-name>
    </security-role>
    <security-role>
        <role-name>DEVELOPERS</role-name>
    </security-role>

```
- Configyre una clase para especificar seguridad mediante identityStore

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;

/**
 *
 * @author avbravo
 */
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/home.xhtml",
                useForwardToLogin = false
            )
)
@FacesConfig
@ApplicationScoped
public class ApplicationConfig {
    
}
```

- Cree la clase que implemente IdentityStore
```java
import com.avbravo.jmoordbutils.ConsoleUtil;
import com.avbravo.jmoordbutils.FacesUtil;
import com.avbravo.jmoordbutils.JmoordbCoreContext;
import com.avbravo.jmoordbutils.JmoordbResourcesFiles;
import com.sft.model.Profile;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author avbravo
 */
//@Named(value = "customInMemoryIdentityStore")
@Named()
@ApplicationScoped
public class SecuriryIdentityStore implements IdentityStore {

    private String roleForWebSecurity = " ";
    @Inject
    JmoordbResourcesFiles rf;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        ConsoleUtil.test("====================================");
        ConsoleUtil.test(">>>>estoy en SecuriryInMemoryIdentityStore "+new Date());
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;

   
        ConsoleUtil.test(">>> login.getCaller() " + login.getCaller() + "login.getPasswordAsString() " + login.getPasswordAsString());

        String username = login.getCaller();
        String password = login.getPasswordAsString();

        if (!isValidData(username, password)) {

            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        /**
         * Obtiene el rol que se obtuvo en el login
         */
        Profile profile = (Profile) JmoordbCoreContext.get("LoginFaces_profileLogged");
        if (profile == null || profile.getId() == null) {
             ConsoleUtil.test(">>>>>>El profile is null ");
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        roleForWebSecurity = profile.getRole().getRole();
    ConsoleUtil.test("El rol es " + roleForWebSecurity);
        return new CredentialValidationResult(username, new HashSet<>(Arrays.asList(roleForWebSecurity)));

    }
//

    // <editor-fold defaultstate="collapsed" desc="Boolean isValidData(String username, String password)">
    private Boolean isValidData(String username, String password) {
        try {
            if (username.isEmpty() || username.equals("") || username == null) {
                FacesUtil.warningMessage(rf.fromCore("warning.usernameisempty"));
                return false;
            }
            if (password.isEmpty() || password.equals("") || password == null) {
                FacesUtil.warningMessage(rf.fromCore("warning.passwordisempty"));
                return false;
            }
            return true;
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfMethod() + " " + e.getLocalizedMessage());
        }

        return false;
    }
    // </editor-fold>

}


```



## Incoqie desde el LoginFaces

- Cree el metotodo
```java
      // <editor-fold defaultstate="collapsed" desc="AuthenticationStatus continueAuthentication()()">
    private AuthenticationStatus continueAuthentication() {
        
        ConsoleUtil.test("=======================================================");
        ConsoleUtil.test("[] estoy en continueAuthentication() at "+new Date());
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams()
                        .credential(new UsernamePasswordCredential(username, password))
        );
    }

// </editor-fold>

```

- Realice las invocaciones mediante

```java

public String login() {
        try {
ConsoleUtil.test("[login()]......at "+new Date());
            if (password == null || password.equals("")) {
                FacesUtil.warningMessage(rf.fromCore("warning.passwordisempty"));
                return "";
            }

            if (profileLogged == null || profileLogged.getRole() == null) {
                FacesUtil.warningMessage(rf.fromCore("warning.profilenotselected"));
                return "";
            }

            /**
             * Validar el password
             */
            // Desencriptar el password de la base de datos
            String passwordDecrypter = Encryptor.decrypt(userLogged.getPassword(), secretKey);

            if (passwordDecrypter.equals(password)) {
                //AplicarIdentityStore
            } else {
                FacesUtil.warningMessage(rf.fromCore("warning.passwordnotmatch"));
                return "";
            }

            ConsoleUtil.test("[]Voy a guardar el profileLoogged en el JmoordbCoreContext");
            /**
             * Guarda el role en el Conext
             */
            JmoordbCoreContext.put("LoginFaces_profileLogged",profileLogged);
           /**
             * Verificar la Seguridad de la sesion
             */
//---Injectarlo en el Session
ConsoleUtil.test("[]Voy a invocar continueAuthentication()......");
            switch (continueAuthentication()) {
                case SEND_CONTINUE:
                    ConsoleUtil.test("SEND_CONTINUE");
                    facesContext.responseComplete();
                    break;
                case SEND_FAILURE:
                    ConsoleUtil.test("SEND_FAILURE");
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
                    break;
                case SUCCESS:
                    ConsoleUtil.test("SEND_SUCCESS");
//                    loggedIn = true;
//                    user = (User) JmoordbCoreContext.get("jmoordb_user");
//
//                    //Guarda el registro del acceso
//                    String ip = JsfUtil.getIp() == null ? "" : JsfUtil.getIp();
//                    Access access = new Access.Builder()
//                            .idaccess(0)
//                            .date(DateUtil.getFechaHoraActual())
//                            .ip(ip)
//                            .username(username)
//                            .idapplicative(applicativeId.get())
//                            .event("login")
//                            .iddepartament(profile.getIddepartament())
//                            .idprofile(profile.getIdprofile())
//                            .idrole(profile.getIdrole())
//                            .build();
//
//                    accessEvent.fire(new AccessEvent(access));
//
//                    loggedIn = true;
                    // Lo guardo en la sesion
//            
                    //Veririco si tiene notificaciones

//                    if (verificarNotifications(user.getUsername())) {
//
//                        JsfUtil.successMessage(rf.getAppMessage("login.welcome") + " " + user.getName() + " " + rf.getAppMessage("login.havenotifications"));
//
//                    } else {
//
//                        JsfUtil.successMessage(rf.getAppMessage("login.welcome") + " " + user.getName());
//                    }
//                    //  JsfUtil.successMessage(rf.getAppMessage("login.welcome") + " " + user.getName());
//
////                    if ( profile.getIdrole().equals(rolColaborador.get())) {
////                        return "/faces/index.xhtml?faces-redirect=true";
////                    }
//                    
//                      JmoordbContext.put("pageInView", "/faces/index.xhtml");
//                      System.out.println("Test-->login() /faces/index.xhtml");
                  // return "/faces/index.xhtml?faces-redirect=true";
                 // return "/faces/index.xhtml";
return "";
                case NOT_DONE:
                    ConsoleUtil.test("NOT_DONE");
            }

            
        } catch (Exception e) {
            FacesUtil.errorMessage(FacesUtil.nameOfMethod() + " " + e.getLocalizedMessage());
        }
        return "";
    }

    // </editor-fold>
```


