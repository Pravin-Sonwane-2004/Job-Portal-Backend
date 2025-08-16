//package com.pravin.job_portal_backend.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/api/jobs/external")
//public class ExternalJobController {
//
//    private final ExternalJobService externalJobService;
//
//    @Autowired
//    public ExternalJobController(ExternalJobService externalJobService) {
//        this.externalJobService = externalJobService;
//    }
//
//    @GetMapping
//    public Mono<ResponseEntity<String>> getExternalJobs() {
//        return externalJobService.fetchExternalJobs()
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.noContent().build());
//    }
//}
