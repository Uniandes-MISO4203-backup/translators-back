package co.edu.uniandes.csw.translationservice.services;

import co.edu.uniandes.csw.auth.provider.StatusCreated;
import static co.edu.uniandes.csw.auth.stormpath.Utils.getClient;
import co.edu.uniandes.csw.translationservice.api.ICustomerLogic;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import co.edu.uniandes.csw.translationservice.api.ITranslatorOfertLogic;
import co.edu.uniandes.csw.translationservice.api.ITranslatorLogic;
import co.edu.uniandes.csw.translationservice.dtos.TranslatorOfertDTO;
import co.edu.uniandes.csw.translationservice.entities.TranslatorOfertEntity;
import co.edu.uniandes.csw.translationservice.converters.TranslatorOfertConverter;
import co.edu.uniandes.csw.translationservice.converters.TranslatorConverter;
import co.edu.uniandes.csw.translationservice.dtos.TranslatorDTO;
import static co.edu.uniandes.csw.translationservice.services.AccountService.getCurrentTranslator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.group.Group;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;


/**
 * @generated
 */
@Path("/translatorOferts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TranslatorOfertService {

    @Inject
    private ITranslatorOfertLogic translatorOfertLogic;
    @Inject
    private ICustomerLogic customerLogic;
    @Context
    private HttpServletRequest req;
    @Context
    private HttpServletResponse response;
    @QueryParam("page")
    private Integer page;
    @QueryParam("maxRecords")
    private Integer maxRecords;
    
    private static final int MAX_EMAIL =40;
    
    @Inject private ITranslatorLogic translatorLogic;

    /**
     * Obtiene la lista de los registros de TranslatorOfert.
     *
     * @return Colección de objetos de TranslatorOfertDTO cada uno con sus
     * respectivos Review
     * @generated
     */
    @GET
    public List<TranslatorOfertDTO> getTranslatorOferts() {
        Long id = getCurrentTranslator(req.getRemoteUser()).getId();
        return TranslatorOfertConverter.listEntity2DTO(translatorLogic.getTranslator(id).getTranslatorOferts());
    }

    /**
     * Obtiene los datos de una instancia de TranslatorOfert a partir de su ID.
     *
     * @param id Identificador de la instancia a consultar
     * @return Instancia de TranslatorOfertDTO con los datos del TranslatorOfert
     * consultado y sus Review
     * @generated
     */
    @GET
    @Path("{id: \\d+}")
    public TranslatorOfertDTO getTranslatorOfert(@PathParam("id") Long id) {
        return TranslatorOfertConverter.fullEntity2DTO(translatorOfertLogic.getTranslatorOfert(id));
    }

    /**
     * Se encarga de crear un TranslatorOfert en la base de datos.
     *
     * @param dto Objeto de TranslatorOfertDTO con los datos nuevos
     * @return Objeto de TranslatorOfertDTO con los datos nuevos y su ID.
     * @generated
     */
    @POST
    @StatusCreated
    public TranslatorOfertDTO createTranslatorOfert(TranslatorOfertDTO dto) {
        TranslatorOfertEntity entity = TranslatorOfertConverter.fullDTO2Entity(dto);
        entity.setTranslator(getCurrentTranslator(req.getRemoteUser()));
        
        String[] to = new String[MAX_EMAIL];
        to[0]= "ing.rojas.m@gmail.com";
        
        String subject = "New Translator Ofert was created ";
       
        String body = "Hi Customer, a new TranslatorOfert has been created";
        
        System.out.println("body: "+body);
        
        
        System.out.println("to: "+to);
        for(String it:to){
            System.out.println("tounit: "+it);
            
        }
        MailService.sendMailAdmin(to, subject, body);

        return TranslatorOfertConverter.fullEntity2DTO(translatorOfertLogic.createTranslatorOfert(entity));
    }

    /**
     * Actualiza la información de una instancia de Book.
     *
     * @param id Identificador de la instancia de Book a modificar
     * @param dto Instancia de TranslatorOfertDTO con los nuevos datos.
     * @return Instancia de TranslatorOfertDTO con los datos actualizados.
     * @generated
     */
    @PUT
    @Path("{id: \\d+}")
    public TranslatorOfertDTO updateTranslatorOfert(@PathParam("id") Long id, TranslatorOfertDTO dto) {
        TranslatorOfertEntity entity = TranslatorOfertConverter.fullDTO2Entity(dto);
        entity.setId(id);
        return TranslatorOfertConverter.fullEntity2DTO(translatorOfertLogic.updateTranslatorOfert(entity));
    }

    /**
     * Elimina una instancia de Book de la base de datos.
     *
     * @param id Identificador de la instancia a eliminar.
     * @generated
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteTranslatorOfert(@PathParam("id") Long id) {
        translatorOfertLogic.deleteTranslatorOfert(id);
    }
}
