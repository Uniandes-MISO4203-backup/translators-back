package co.edu.uniandes.csw.translationservice.tests;

import co.edu.uniandes.csw.auth.model.UserDTO;
import co.edu.uniandes.csw.auth.security.JWT;
import co.edu.uniandes.csw.translationservice.dtos.CustomerDTO;
import co.edu.uniandes.csw.translationservice.dtos.TranslationRequestDTO;
import co.edu.uniandes.csw.translationservice.services.TranslationRequestService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class TranslationRequestTest {

    private final int Ok = Status.OK.getStatusCode();
    private final int Created = Status.CREATED.getStatusCode();
    private final int OkWithoutContent = Status.NO_CONTENT.getStatusCode();
    private final String translationRequestPath = "translationRequests";
    private final String customerPath = "customers";
    private final static List<TranslationRequestDTO> oraculo = new ArrayList<>();
    private WebTarget target;
    private final String apiPath = "api";
    private final String username = System.getenv("USERNAME_USER");
    private final String password = System.getenv("PASSWORD_USER");

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                // Se agrega la dependencia a la logica con el nombre groupid:artefactid:version (GAV)
                .addAsLibraries(Maven.resolver()
                        .resolve("co.edu.uniandes.csw.translationservice:translation-service-logic:0.1.0")
                        .withTransitivity().asFile())
                .addAsLibraries(Maven.resolver()
                        .resolve("co.edu.uniandes.csw:auth-utils:0.1.0")
                        .withTransitivity().asFile())
                // Se agregan los compilados de los paquetes de servicios
                .addPackage(TranslationRequestService.class.getPackage())
                // El archivo que contiene la configuracion a la base de datos.
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                // El archivo beans.xml es necesario para injeccion de dependencias.
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                // El archivo shiro.ini es necesario para injeccion de dependencias
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/shiro.ini"))
                // El archivo web.xml es necesario para el despliegue de los servlets
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    private WebTarget createWebTarget() {
        ClientConfig config = new ClientConfig();
        config.register(LoggingFilter.class);
        return ClientBuilder.newClient(config).target(deploymentURL.toString()).path(apiPath);
    }

    @BeforeClass
    public static void setUp() {
        insertData();
    }

    public static void insertData() {
        for (int i = 0; i < 5; i++) {
            PodamFactory factory = new PodamFactoryImpl();
            TranslationRequestDTO translationRequest = factory.manufacturePojo(TranslationRequestDTO.class);
            translationRequest.setId(i + 1L);

            oraculo.add(translationRequest);

        }
    }

    public Cookie login(String username, String password) {
        UserDTO user = new UserDTO();
        user.setUserName(username);
        user.setPassword(password);
        user.setRememberMe(true);
        Response response = target.path("users").path("login").request()
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Ok) {
            return response.getCookies().get(JWT.cookieName);
        } else {
            return null;
        }
    }

    @Before
    public void setUpTest() {
        target = createWebTarget();
    }

    @Test
    @InSequence(1)
    public void createTranslationRequestTest() throws IOException {
        PodamFactory factory = new PodamFactoryImpl();
        CustomerDTO customer = factory.manufacturePojo(CustomerDTO.class);;
        Cookie cookieSessionId = login(username, password);
        target.path(customerPath)
                .request().cookie(cookieSessionId)
                .post(Entity.entity(customer, MediaType.APPLICATION_JSON));

        TranslationRequestDTO translationRequest = oraculo.get(0);
        Response response = target.path(translationRequestPath)
                .request().cookie(cookieSessionId)
                .post(Entity.entity(translationRequest, MediaType.APPLICATION_JSON));
        TranslationRequestDTO translationrequestTest = (TranslationRequestDTO) response.readEntity(TranslationRequestDTO.class);
        if(translationRequest==null)
            System.err.println("null uno");
        if(translationrequestTest==null)
            System.err.println("null dos");
        Assert.assertEquals(translationRequest.getId(), translationrequestTest.getId());
        Assert.assertEquals(translationRequest.getName(), translationrequestTest.getName());
        Assert.assertEquals(translationRequest.getCreationDate(), translationrequestTest.getCreationDate());
        Assert.assertEquals(translationRequest.getDueDate(), translationrequestTest.getDueDate());
        Assert.assertEquals(translationRequest.getContexto(), translationrequestTest.getContexto());
//        Assert.assertEquals(translationRequest.getDescription(), translationrequestTest.getDescription());
//        Assert.assertEquals(translationRequest.getNumberOfWords(), translationrequestTest.getNumberOfWords());
        Assert.assertEquals(Created, response.getStatus());
    }

    @Test
    @InSequence(2)
    public void getTranslationRequestById() {
        Cookie cookieSessionId = login(username, password);
        TranslationRequestDTO translationrequestTest = target.path(translationRequestPath)
                .path(oraculo.get(0).getId().toString())
                .request().cookie(cookieSessionId).get(TranslationRequestDTO.class);
        
        if(oraculo.get(0)==null)
            System.err.println("null unos "+oraculo.get(0));
        if(translationrequestTest==null)
            System.err.println("null doss ");
        
        Assert.assertEquals(translationrequestTest.getId(), oraculo.get(0).getId());
        Assert.assertEquals(translationrequestTest.getName(), oraculo.get(0).getName());
        Assert.assertEquals(translationrequestTest.getCreationDate(), oraculo.get(0).getCreationDate());
        Assert.assertEquals(translationrequestTest.getDueDate(), oraculo.get(0).getDueDate());
        Assert.assertEquals(translationrequestTest.getContexto(), oraculo.get(0).getContexto());
//        Assert.assertEquals(translationrequestTest.getDescription(), oraculo.get(0).getDescription());
//        Assert.assertEquals(translationrequestTest.getNumberOfWords(), oraculo.get(0).getNumberOfWords());
    }

    @Test
    @InSequence(3)
    public void listTranslationRequestTest() throws IOException {
        Cookie cookieSessionId = login(username, password);
        Response response = target.path(translationRequestPath)
                .request().cookie(cookieSessionId).get();
        String listTranslationRequest = response.readEntity(String.class);
        List<TranslationRequestDTO> listTranslationRequestTest = new ObjectMapper().readValue(listTranslationRequest, List.class);
        Assert.assertEquals(Ok, response.getStatus());
        Assert.assertEquals(1, listTranslationRequestTest.size());
    }

    @Test
    @InSequence(4)
    public void updateTranslationRequestTest() throws IOException {
        Cookie cookieSessionId = login(username, password);
        TranslationRequestDTO translationRequest = oraculo.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        TranslationRequestDTO translationRequestChanged = factory.manufacturePojo(TranslationRequestDTO.class);
        translationRequest.setName(translationRequestChanged.getName());
        translationRequest.setCreationDate(translationRequestChanged.getCreationDate());
        translationRequest.setDueDate(translationRequestChanged.getDueDate());
        translationRequest.setContexto(translationRequestChanged.getContexto());
//        translationRequest.setDescription(translationRequestChanged.getDescription());
//        translationRequest.setNumberOfWords(translationRequestChanged.getNumberOfWords());
        Response response = target.path(translationRequestPath).path(translationRequest.getId().toString())
                .request().cookie(cookieSessionId).put(Entity.entity(translationRequest, MediaType.APPLICATION_JSON));
        TranslationRequestDTO translationrequestTest = (TranslationRequestDTO) response.readEntity(TranslationRequestDTO.class);
        Assert.assertEquals(Ok, response.getStatus());
        Assert.assertEquals(translationRequest.getName(), translationrequestTest.getName());
        Assert.assertEquals(translationRequest.getCreationDate(), translationrequestTest.getCreationDate());
        Assert.assertEquals(translationRequest.getDueDate(), translationrequestTest.getDueDate());
        Assert.assertEquals(translationRequest.getContexto(), translationrequestTest.getContexto());
//        Assert.assertEquals(translationRequest.getDescription(), translationrequestTest.getDescription());
//        Assert.assertEquals(translationRequest.getNumberOfWords(), translationrequestTest.getNumberOfWords());
    }

    @Test
    @InSequence(5)
    public void deleteTranslationRequestTest() {
        Cookie cookieSessionId = login(username, password);
        TranslationRequestDTO translationRequest = oraculo.get(0);
        Response response = target.path(translationRequestPath).path(translationRequest.getId().toString())
                .request().cookie(cookieSessionId).delete();
        Assert.assertEquals(OkWithoutContent, response.getStatus());
    }
}
