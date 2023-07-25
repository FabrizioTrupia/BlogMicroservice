package it.cgmconsulting.mspost.controller;

import com.itextpdf.svg.converter.SvgConverter;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.service.PdfService;
import it.cgmconsulting.mspost.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class PdfController {

    private final PostService postService;
    private final PdfService pdfService;


    @GetMapping("/v0/get-pdf/{postId}")
    public ResponseEntity<?> getPdf(@PathVariable long postId){

        PostResponse p = postService.getPostById(postId);
        if(p == null)
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        InputStream pdfFile = null;
        ResponseEntity<InputStreamResource> response = null;

        try{
            pdfFile = pdfService.createPdf(p);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Access-Control-Allow-Origin", "*");
            httpHeaders.add("Access-Control-Allow-Method", "GET");
            httpHeaders.add("Access-Control-Allow-Header", "Content-Type");
            httpHeaders.add("Cache-Control","no-cache, no-store, must-revalidate");
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            httpHeaders.add("Content-Disposition", "inline; filename="+postId+".pdf");

            response = new ResponseEntity<InputStreamResource>(
                    new InputStreamResource(pdfFile),
                    httpHeaders,
                    HttpStatus.OK
            );

        } catch(Exception e){
            response = new ResponseEntity<InputStreamResource>(
                    new InputStreamResource(null, "Something went wrong creating the pdf"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return response;
    }



}
