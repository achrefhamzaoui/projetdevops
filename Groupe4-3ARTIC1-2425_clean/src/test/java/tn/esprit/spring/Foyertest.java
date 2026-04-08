package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.*;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Foyertest {

    @InjectMocks
    FoyerService foyerService; // Mockito va injecter les mocks ici

    @Mock
    FoyerRepository foyerRepository;

    @Mock
    UniversiteRepository universiteRepository;

    @Mock
    BlocRepository blocRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initialise les mocks
    }

    @Test
    void testAddOrUpdate() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer saved = foyerService.addOrUpdate(foyer);

        assertEquals(foyer, saved);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testFindAll() {
        List<Foyer> foyers = List.of(new Foyer(), new Foyer());
        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.findAll();

        assertEquals(2, result.size());
        verify(foyerRepository).findAll();
    }

    @Test
    void testFindById() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L); // fonctionne si Lombok est bien activé avec @Setter
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdFoyer());
    }

    @Test
    void testDeleteById() {
        foyerService.deleteById(1L);
        verify(foyerRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        Foyer foyer = new Foyer();
        foyerService.delete(foyer);
        verify(foyerRepository).delete(foyer);
    }

    @Test
    void testAffecterFoyerAUniversiteByName() {
        Foyer foyer = new Foyer();
        Universite universite = new Universite();

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("ESPRIT")).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, "ESPRIT");

        assertEquals(foyer, result.getFoyer());
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Foyer foyer = new Foyer();
        foyer.setBlocs(List.of(new Bloc(), new Bloc()));
        Universite universite = new Universite();

        when(foyerRepository.save(any())).thenReturn(foyer);
        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(universite));
        when(blocRepository.save(any())).thenReturn(new Bloc());
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 2L);

        assertNotNull(result);
        verify(blocRepository, times(2)).save(any());
        verify(universiteRepository).save(universite);
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        Foyer foyer = new Foyer();
        foyer.setBlocs(List.of(bloc1, bloc2));

        when(foyerRepository.save(any())).thenReturn(foyer);
        when(blocRepository.save(any())).thenReturn(bloc1);

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer);

        assertEquals(foyer, result);
        verify(blocRepository, times(2)).save(any());
    }

    @Test
    void testAffecterFoyerAUniversiteByIds() {
        Foyer foyer = new Foyer();
        Universite universite = new Universite();

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findById(2L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, 2L);

        assertEquals(foyer, result.getFoyer());
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Universite universite = new Universite();
        universite.setFoyer(new Foyer());

        when(universiteRepository.findById(3L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.desaffecterFoyerAUniversite(3L);

        assertNull(result.getFoyer());
    }
}
