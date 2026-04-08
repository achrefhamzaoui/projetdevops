package tn.esprit.spring.Services.Foyer;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {
    @Autowired
    private FoyerRepository foyerRepository;
    @Autowired
    private  UniversiteRepository universiteRepository;
    @Autowired
    private  BlocRepository blocRepository;

    @Override
    public Foyer addOrUpdate(Foyer f) {
        return foyerRepository.save(f);
    }

    @Override
    public List<Foyer> findAll() {
        return foyerRepository.findAll();
    }

    @Override
    public Foyer findById(long id) {
        return foyerRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        foyerRepository.deleteById(id);
    }

    @Override
    public void delete(Foyer f) {
        foyerRepository.delete(f);
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Foyer f = findById(idFoyer); // Child
        Universite u = universiteRepository.findByNomUniversite(nomUniversite); // Parent
        // On affecte le child au parent
        u.setFoyer(f);
        return universiteRepository.save(u);
    }


    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        // Récuperer la liste des blocs avant de faire l'ajout
        List<Bloc> blocs = foyer.getBlocs();
        // Foyer est le child et universite est parent
        Foyer f = foyerRepository.save(foyer);
        Universite u = universiteRepository.findById(idUniversite).get();
        // Foyer est le child et bloc est le parent
        //On affecte le child au parent
        for (Bloc bloc : blocs) {
            bloc.setFoyer(foyer);
            blocRepository.save(bloc);
        }
        u.setFoyer(f);
        return universiteRepository.save(u).getFoyer();
    }

    @Override
    public Foyer ajoutFoyerEtBlocs(Foyer foyer) {
        //Foyer child / Bloc parent
        //Objet foyer = attribut objet foyer + les blocs associés
//        Foyer f = repo.save(foyer);
//        for (Bloc b : foyer.getBlocs()) {
//            b.setFoyer(f);
//            blocRepository.save(b);
//        }
//        return f;
        //-----------------------------------------
        List<Bloc> blocs = foyer.getBlocs();
        foyer = foyerRepository.save(foyer);
        for (Bloc b : blocs) {
            b.setFoyer(foyer);
            blocRepository.save(b);
        }
        return foyer;
    }

    @Override
    public Universite affecterFoyerAUniversite(long idF, long idU) {
        Universite u= universiteRepository.findById(idU).get();
        Foyer f= foyerRepository.findById(idF).get();
        u.setFoyer(f);
        return universiteRepository.save(u);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite u = universiteRepository.findById(idUniversite).get(); // Parent
        u.setFoyer(null);
        return universiteRepository.save(u);
    }


}
