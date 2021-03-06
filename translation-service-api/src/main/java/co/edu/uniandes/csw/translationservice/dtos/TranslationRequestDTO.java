package co.edu.uniandes.csw.translationservice.dtos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * @generated
 */
@XmlRootElement
public class TranslationRequestDTO extends RequestDTO {

    @PodamExclude
    private LanguageDTO originalLanguage;
    @PodamExclude
    private LanguageDTO targetLanguage;
    @PodamExclude
    private String description;
    @PodamExclude
    private String contexto;
    @PodamExclude
    private int numberOfWords;
    
    @PodamExclude
    private List<KnowledgeAreaDTO> knowledgeAreas = new ArrayList<>();
    
    @PodamExclude
    private List<TranslatorOfertDTO> translatorOferts = new ArrayList<>();
    
    @PodamExclude
    private String enlaceArchivoResultado;
    
    @PodamExclude
    private String urlFile;

    /**
     * @generated
     */
    public LanguageDTO getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * @generated
     */
    public void setOriginalLanguage(LanguageDTO originallanguage) {
        this.originalLanguage = originallanguage;
    }

    /**
     * @generated
     */
    public LanguageDTO getTargetLanguage() {
        return targetLanguage;
    }

    /**
     * @generated
     */
    public void setTargetLanguage(LanguageDTO targetlanguage) {
        this.targetLanguage = targetlanguage;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the contexto
     */
    public String getContexto() {
        return contexto;
    }

    /**
     * @param contexto the contexto to set
     */
    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    /**
     * @return the knowledgeAreas
     */
    public List<KnowledgeAreaDTO> getKnowledgeAreas() {
        return knowledgeAreas;
    }

    /**
     * @param knowledgeAreas the knowledgeAreas to set
     */
    public void setKnowledgeAreas(List<KnowledgeAreaDTO> knowledgeAreas) {
        this.knowledgeAreas = knowledgeAreas;
    }
    
            
    /**
     * @return the translatorOferts
     */
    public List<TranslatorOfertDTO> getTranslatorOferts() {
        return translatorOferts;
    }

    /**
     * @param translatorOferts the translatorOferts to set
     */
    public void setTranslatorOferts(List<TranslatorOfertDTO> translatorOferts) {
        this.translatorOferts = translatorOferts;
    }

    public String getEnlaceArchivoResultado() {
        return enlaceArchivoResultado;
    }

    public void setEnlaceArchivoResultado(String enlaceArchivoResultado) {
        this.enlaceArchivoResultado = enlaceArchivoResultado;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }
    
}
