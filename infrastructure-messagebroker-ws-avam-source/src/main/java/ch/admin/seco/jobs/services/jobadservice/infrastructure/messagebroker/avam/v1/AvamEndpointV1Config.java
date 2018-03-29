package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v1;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v1.JobAdvertisementFromAvamAssemblerV1;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class AvamEndpointV1Config extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/services/*");
    }

    @Bean(name = "SecoEgovService")
    public Wsdl11Definition secoEgovServiceWsdl11Definition() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/schema/v1/AVAMToEgov.wsdl")); //your schema location
        return wsdl11Definition;
    }

    @Bean(name = "SecoEgovServiceXsd")
    public XsdSchema secoEgovSchema() {
        return new SimpleXsdSchema(new ClassPathResource("/schema/v1/AVAMToEgov.xsd"));
    }

    @Bean
    public JobAdvertisementFromAvamAssemblerV1 jobAdvertisementFromAvamAssembler() {
        return new JobAdvertisementFromAvamAssemblerV1();
    }
}
