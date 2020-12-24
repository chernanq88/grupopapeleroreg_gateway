package mx.com.tigo.grupopapelero.gateway.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

@org.springframework.context.annotation.Configuration
public class FreemarkerConfig{

	@Value("${grupopapeleroreg.url.static}")
	private String contenidoEstatico;
	
	@Bean
	public Configuration configuration() throws IOException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_19);
		cfg.setDirectoryForTemplateLoading(new File(contenidoEstatico + "/templates")); 
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		
		return cfg;
	}
	
}
