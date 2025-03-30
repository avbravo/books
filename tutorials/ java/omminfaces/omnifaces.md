## Ommifaces 
Util
https://showcase.omnifaces.org/utils/Faces
Utilidades como cookies manejo de sesiones

Funciones para mensajes, manejo de Cookies

Obtener valor de una cokie
// Get a cookie value.
String cookieValue = Faces.getRequestCookie("cookieName");

Guardar valor en una cookie
    private void storeCookieCredentials(final String email, final String password) {
       Faces.addResponseCookie("admin-email", email, 1800);//store for 30min
       Faces.addResponseCookie("admin-pass", password, 1800);//store for 30min
    }


Logout
// Invalidate the session and send a redirect.
public void logout() {
    Faces.invalidateSession();
    Faces.redirect("login.xhtml"); // Can by the way also be done by return "login?faces-redirect=true" if in action method.
}