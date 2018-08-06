package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.ElasticsearchIndexService.INDEX_NAME_JOB_ADVERTISEMENT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.ElasticsearchIndexService.TYPE_JOB_ADVERTISEMENT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

@Document(indexName = INDEX_NAME_JOB_ADVERTISEMENT, type = TYPE_JOB_ADVERTISEMENT)
@Mapping(mappingPath = "config/elasticsearch/mappings/job-advertisement.json")
@Setting(settingPath = "config/elasticsearch/settings/folding-analyzer.json")
public class JobAdvertisementDocument {
    
    private static final Map<String, String> languageSynonyms = new HashMap<>();

    static {
        languageSynonyms.put("ar", "arabisch,arab,arabe,arabo");
        languageSynonyms.put("bg", "bulgarisch,bulgarian,bulgare,bulgaro");
        languageSynonyms.put("bs", "bosnisch,bosnian,bosniaque,bosniaco");
        languageSynonyms.put("ch", "schweizerdeutsch,swiss german,suisse allemand,svizzero tedesco");
        languageSynonyms.put("cs", "tschechisch,czech,tchèque,ceco");
        languageSynonyms.put("da", "dänisch,danish,danois,danese");
        languageSynonyms.put("de", "deutsch,german,allemand,tedesco");
        languageSynonyms.put("de-ch", "schweizerdeutsch,swiss german,suisse allemand,svizzero tedesco");
        languageSynonyms.put("el", "griechisch,greek,grec,greco");
        languageSynonyms.put("en", "englisch,english,anglais,inglese");
        languageSynonyms.put("es", "spanisch,spanish,espagnol,spagnolo");
        languageSynonyms.put("fi", "finnisch,finnish,finlandais,finlandese");
        languageSynonyms.put("fr", "französisch,french,français,francese");
        languageSynonyms.put("he", "hebräisch,hebrew,hébraïque,ebreo");
        languageSynonyms.put("hr", "kroatisch,croatian,croate,croato");
        languageSynonyms.put("hu", "ungarisch,hungarian,hongrois,ungherese");
        languageSynonyms.put("it", "italienisch,italian,italien,italiano");
        languageSynonyms.put("ja", "japanisch,japanese,japonais,giapponese");
        languageSynonyms.put("km", "khmer,khmer,khmer,khmer");
        languageSynonyms.put("ku", "kurdisch,kurdish,kurde,curdo");
        languageSynonyms.put("lt", "litauisch,lithuanian,lituanien,lituano");
        languageSynonyms.put("mk", "mazedonisch,macedonian,macédonien,macedone");
        languageSynonyms.put("nl", "holländisch,dutch,hollandais,olandese");
        languageSynonyms.put("no", "norwegisch,norvegian,norvégien,norvegese");
        languageSynonyms.put("pl", "polnisch,polish,polonais,polacco");
        languageSynonyms.put("pt", "portugiesisch,portuguese,portugais,portoghese");
        languageSynonyms.put("rm", "rätoromanisch,rhaeto-romanic,rheto-romanche,reto-romancio");
        languageSynonyms.put("ro", "rumänisch,romanian,roumain,rumeno");
        languageSynonyms.put("ru", "russisch,russian,russe,russo");
        languageSynonyms.put("sk", "slowakisch,slovakian,slovaque,slovacco");
        languageSynonyms.put("sl", "slowenisch,slovenian,slovène,sloveno");
        languageSynonyms.put("sq", "albanisch,albanian,albanais,albanese");
        languageSynonyms.put("sr", "serbisch,serbian,serbe,serbo");
        languageSynonyms.put("sv", "schwedisch,swedish,suédois,svedese");
        languageSynonyms.put("ta", "tamil,tamil,tamoul,tamil");
        languageSynonyms.put("th", "thai,thai,thaïlandais,tailandese");
        languageSynonyms.put("tr", "türkisch,turkish,turc,turco");
        languageSynonyms.put("vi", "vietnamesisch,vietnamese,vietnamien,vietnamita");
        languageSynonyms.put("zh", "chinesisch,chinese,chinois,cinese");
    }

    @Id
    private String id;

    private String languageSkillSynonyms;

    private JobAdvertisement jobAdvertisement;

    protected JobAdvertisementDocument() {
    }

    public JobAdvertisementDocument(JobAdvertisement jobAdvertisement) {
        this.jobAdvertisement = jobAdvertisement;
        this.id = jobAdvertisement.getId().getValue();

        populateLanguageSkillSynonyms();
    }

    private void populateLanguageSkillSynonyms() {
        final List<LanguageSkill> languageSkills = this.jobAdvertisement.getJobContent().getLanguageSkills();
        if (languageSkills == null || languageSkills.isEmpty()) {
            return;
        }

        languageSkills.stream()
                .map(language -> languageSynonyms.get(language.getLanguageIsoCode()))
                .filter(StringUtils::isNotEmpty)
                .reduce(String::concat)
                .ifPresent((synonyms) -> languageSkillSynonyms = synonyms);
    }

    public String getId() {
        return id;
    }

    public String getLanguageSkillSynonyms() {
        return languageSkillSynonyms;
    }

    public JobAdvertisement getJobAdvertisement() {
        return jobAdvertisement;
    }
}
