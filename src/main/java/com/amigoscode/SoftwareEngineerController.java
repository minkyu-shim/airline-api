package com.amigoscode;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/software-engineers")
public class SoftwareEngineerController {
    private final SoftwareEngineerService softwareEngineerService;

    public SoftwareEngineerController(SoftwareEngineerService softwareEngineerService) {
        this.softwareEngineerService = softwareEngineerService;
    }

    // @PostMapping
    // @GetMapping
    // @UpdateMapping
    // @DeleteMapping

    @GetMapping
    public List<SoftwareEngineer> getAllEngineers() {
        return softwareEngineerService.getAllSoftwareEngineers();
    }

    @GetMapping("{id}")
    public SoftwareEngineer getAllEngineerById(@PathVariable Integer id) {
        return softwareEngineerService.getSoftwareEngineerById(id);
    }

    @PostMapping
    public void addNewSoftwareEngineer(@RequestBody SoftwareEngineer softwareEngineer) {
        softwareEngineerService.insertSoftwareEngineer(softwareEngineer);
    }
}
