package org.thebreak.roombooking.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.dao.DictionaryRepository;
import org.thebreak.roombooking.model.Dictionary;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.service.impl.DictionaryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)  // same as using MockitoAnnotations.initMocks(this);
class DictionaryServiceUT {

    @InjectMocks
    DictionaryServiceImpl dictionaryService;  // dependencies with @Mock annotation will be injected to this service

    @Mock
    DictionaryRepository repository;

    @Captor
    ArgumentCaptor<Pageable> pageCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    private List<Dictionary> getMockedDictionaryList() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name1");
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        dictionary.setValues(list);

        List<Dictionary> dictsList= new ArrayList<>();
        dictsList.add(dictionary);

        return dictsList;
    }

    @Test
    void addDictionary_withValidDictionaryFields_shouldReturnDictionary() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name2");
        when(repository.findByName(any())).thenReturn(null);
        when(repository.save(any(Dictionary.class))).thenReturn(dictionary);
       dictionaryService.addDictionary("name2");
        verify(repository).save(any(Dictionary.class));
    }

    @Test
    void addDictionary_withMissingNameFields_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.addDictionary(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void addDictionary_withEmptyNameField_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.addDictionary("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
    }

    @Test
    void addDictionary_withExistedName_shouldThrowExistException() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name2");
        when(repository.findByName("name2")).thenReturn(dictionary);
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.addDictionary("name2")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_ALREADY_EXIST);
    }

    @Test
    void findDictionaryPage_withoutPageAndSize_shouldUseDefaultPageAndSize() {
        List<Dictionary> dictionaryListList = getMockedDictionaryList();

        // use PageImpl to create paged room list;
        Page<Dictionary> pagedDictionary = new PageImpl(dictionaryListList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedDictionary);

        // controller default is page=1, size=DEFAULT_PAGE_SIZE if no params provided;
        List<Dictionary> findRoomList = dictionaryService.findPage(1, Constants.DEFAULT_PAGE_SIZE).getContent();

        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size are 0 and 5 by default(1 and 5 default by controller)
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(Constants.DEFAULT_PAGE_SIZE);

        // check the return list size is correct
        assertThat(findRoomList.size()).isEqualTo(1);
    }
    @Test
    void findDictionaryPage_withPageAndSize_shouldListCorrectPageAndSize() {
        List<Dictionary> dictList = getMockedDictionaryList();

        // use PageImpl to create paged room list;
        Page<Dictionary> pagedRooms = new PageImpl(dictList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        List<Dictionary> findRoomList = dictionaryService.findPage(1, 5).getContent();

        // check that the repository method is called with correct page and size;
        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(5);

        // check the return list size is correct
        assertThat(findRoomList.size()).isEqualTo(1);
    }

    @Test
    void findDictionaryPage_withSizeExceedingMaxSizeLimit_shouldUseDefinedMaxSize() {
        List<Dictionary> dictList = getMockedDictionaryList();

        // use PageImpl to create paged room list;
        Page<Dictionary> pagedRooms = new PageImpl(dictList);

        when(repository.findAll(any(Pageable.class))).thenReturn(pagedRooms);

        // pass in a size bigger than Max size (50)
        List<Dictionary> findRoomList = dictionaryService.findPage(1, 200).getContent();

        // check that the repository method is called with max size limit;
        //use pageCaptor to capture the arguments called with
        verify(repository).findAll(pageCaptor.capture());
        // get the captured pageable arguments
        Pageable page = pageCaptor.getValue();
        // verify that the page and size
        assertThat(page.getPageNumber()).isEqualTo(0);
        assertThat(page.getPageSize()).isEqualTo(Constants.MAX_PAGE_SIZE);
    }

    @Test
    void findDictionaryById_withValidId_shouldReturnRoom() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name3");
        when(repository.findById(any())).thenReturn(Optional.of(dictionary));

        dictionaryService.findById("validId");

        verify(repository).findById(stringCaptor.capture());
        String id = stringCaptor.getValue();
        assertThat(id).isEqualTo("validId");
    }

    @Test
    void findDictionaryById_withMissingId_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> dictionaryService.findById(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

    @Test
    void findDictionaryById_withEmptyId_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> dictionaryService.findById("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
    }

    @Test
    void findDictionaryById_withInValidId_shouldThrowNotFoundException() {
        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> dictionaryService.findById("notExistRandomID")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_NOT_FOUND);
    }

    @Test
    void deleteDictionaryById_withValidId_shouldReturnSuccess() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name for delete");
        when(repository.findById(any())).thenReturn(Optional.of(dictionary));

        dictionaryService.deleteById("validId");
        verify(repository).deleteById(stringCaptor.capture());
        String id = stringCaptor.getValue();
        assertThat(id).isEqualTo("validId");
    }

    @Test
    void deleteDictionaryById_withInValidId_shouldThrowNotFoundException() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.deleteById("InValidId")).getCommonCode();
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteDictionaryById_withoutIdField_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.deleteById(null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteDictionaryById_withEmptyId_shouldThrowEmptyFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class,() -> dictionaryService.deleteById("")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_EMPTY);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void updateDictionaryById_withValidRoomFields_shouldReturnRoom() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name for delete");
        dictionary.setId("testId123");
        when(repository.findById(any())).thenReturn(Optional.of(dictionary));
        dictionaryService.updateById("testId123","updated name");
        verify(repository).save(dictionary);
    }

    @Test
    void updateDictionaryById_withInValidId_shouldThrowNotFoundException() {
        Dictionary dictionary = new Dictionary();
        dictionary.setName("name for delete");
        dictionary.setId("testId123");
        when(repository.findById(any())).thenReturn(Optional.empty());
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.updateById("randomInvalidId","name")).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.DB_ENTRY_NOT_FOUND);
    }

    @Test
    void updateDictionaryById_withMissingNameField_shouldThrowMissingFieldException() {
        CommonCode code = Assertions.assertThrows(CustomException.class, () -> dictionaryService.updateById("testId123",null)).getCommonCode();
        assertThat(code).isEqualTo(CommonCode.REQUEST_FIELD_MISSING);
    }

}