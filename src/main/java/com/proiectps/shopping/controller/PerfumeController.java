package com.proiectps.shopping.controller;

import com.proiectps.shopping.dto.PerfumeDTO;
import com.proiectps.shopping.export.PerfumeXMLExporter;
import com.proiectps.shopping.model.Perfume;
import com.proiectps.shopping.service.impl.PerfumeServiceImpl;
import lombok.RequiredArgsConstructor;
import com.proiectps.shopping.mapper.PerfumeMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/perfumes")
@RequiredArgsConstructor
public class PerfumeController {


    @Autowired
    private PerfumeServiceImpl perfumeService;

    @GetMapping("/findAll")
    public List<PerfumeDTO> findAllPerfumes(){
        return perfumeService.findAll();
    }

    @GetMapping("/gender/{gender}")
    public List<PerfumeDTO> findByGender(@PathVariable String gender) {
        return perfumeService.findByGender(gender);
    }

    @GetMapping("/brand/{brand}")
    public List<PerfumeDTO> findByBrand(@PathVariable String brand) {
        return perfumeService.findByBrand(brand);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(PerfumeMapper.mapModelToDTO(perfumeService.getPerfumeById(id)));
    }

    @GetMapping("/title/{title}")
    public List<PerfumeDTO> findByTitle(@PathVariable String title) {
        return perfumeService.findAllByTitle(title);
    }

    @PostMapping("/add")
    public ResponseEntity addPerfume(@RequestBody Perfume perfume) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfumeService.savePerfume(perfume));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePerfume(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(perfumeService.deletePerfume(id));
    }

    @PutMapping("/update/{id}/{price}")
    public ResponseEntity updatePrice(@PathVariable Long id, @PathVariable Integer price) {
        return ResponseEntity.status(HttpStatus.OK).body(perfumeService.updatePrice(id, price));
    }

    @GetMapping("/brands")
    public List<String> findAllBrands() {
        return perfumeService.findAllBrands();
    }

    @GetMapping("priceBetween/{minPrice}/{maxPrice}")
    public List<PerfumeDTO> findByPriceBetween(@PathVariable Integer minPrice, @PathVariable Integer maxPrice) {
        return perfumeService.findByPriceBetween(minPrice, maxPrice);
    }

    @GetMapping("/sort")
    public List<PerfumeDTO> sortPerfumesByAsc() {
        return perfumeService.perfumesOrderAsc();
    }

    @GetMapping("/sortDesc")
    public List<PerfumeDTO> sortPerfumesByDesc() {
        return perfumeService.perfumesOrderDesc();
    }

    @GetMapping("/exportPerfumes")
    public ResponseEntity<String> exportPerfumesXML() {
        try {
            List<PerfumeDTO> perfumes = perfumeService.findAll();
            PerfumeXMLExporter.export(perfumes, "perfumes.xml");
            File file = new File("perfumes.xml");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment", "perfumes.xml");
            headers.setContentLength(file.length());
            return new ResponseEntity<>(FileUtils.readFileToString(file, "UTF-8"), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
