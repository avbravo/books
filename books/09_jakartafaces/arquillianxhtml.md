

[https://jakarta.ee/learn/docs/jakartaee-tutorial/current/security/security.html](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/security/security.html)

```
@RunWith(Arquillian.class)
@RunAsClient
public class RestCustomFormAuthCustomStoreIT extends ITBase {

    @ArquillianResource
    private URL baseUrl;

    /**
     * Test the call to a protected REST service
     *
     * @throws Exception when a serious error occurs.
     */
    @RunAsClient
    @Test
    public void testRestCall() throws Exception {
        HtmlPage loginPage = webClient.getPage(baseUrl + "/rest/resource");
        System.out.println(loginPage.asXml());

        HtmlForm form = loginPage.getForms()
                                 .get(0);

        form.getInputByName("form:username")
            .setValueAttribute("john");

        form.getInputByName("form:password")
            .setValueAttribute("secret1");

        TextPage page = form.getInputByValue("Login")
                            .click();

        System.out.println(page.getContent());
    }
}


````