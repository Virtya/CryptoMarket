/*
package com.virtya.CryptoMarket.controller;


import com.virtya.CryptoMarket.entity.Transaction;
import com.virtya.CryptoMarket.model.CurrencyModel;
import com.virtya.CryptoMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
@RequestMapping("/director")
public class DirectorController {

    private final UserService directorService;

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getDirectorById(@PathVariable Long id) {
        return new ResponseEntity<>(directorService.getDirectorById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Transaction>> getDirector(){
        return new ResponseEntity<>(directorService.getDirector(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> addDirector(@RequestBody CurrencyModel directorModel)
    {
        return new ResponseEntity<>(directorService.addDirector(directorModel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateDirector(@PathVariable Long id, @RequestBody CurrencyModel directorModel)
    {
        return new ResponseEntity<>(directorService.updateDirector(id, directorModel), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteById(@PathVariable Long id)
    {
        directorService.deleteById(id);
    }
}
*/
