package in.noteapp.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//            .addResourceHandler("/uploads/**")
//            .addResourceLocations("file:///C:/Users/DELL/NoteUploads/");
//    }
//	  @Override
//	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	        String uploadPath = System.getProperty("user.home") + "/NoteUploads/";
//	        registry.addResourceHandler("/files/**")
//	                .addResourceLocations("file:" + uploadPath);
//	    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = System.getProperty("user.home") + "/NoteUploads/";
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
