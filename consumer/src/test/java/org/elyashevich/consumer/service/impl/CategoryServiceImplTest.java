package org.elyashevich.consumer.service.impl;

import org.elyashevich.consumer.domain.entity.Category;
import org.elyashevich.consumer.exception.ResourceAlreadyExistException;
import org.elyashevich.consumer.exception.ResourceNotFoundException;
import org.elyashevich.consumer.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id("1")
                .name("Test Category")
                .description("Test Description")
                .build();
    }

    // Test data factory methods
    private static Stream<Arguments> provideCategoriesForFindAll() {
        return Stream.of(
                Arguments.of(List.of(), "Empty list"),
                Arguments.of(List.of(
                        Category.builder().name("Cat1").build(),
                        Category.builder().name("Cat2").build()
                ), "Multiple categories"),
                Arguments.of(List.of(
                        Category.builder().name("Single").build()
                ), "Single category")
        );
    }

    private static Stream<Arguments> provideValidCategoryNames() {
        return Stream.of(
                Arguments.of("Existing Category"),
                Arguments.of("Another Category"),
                Arguments.of("Category with spaces"),
                Arguments.of("123Category")
        );
    }

    private static Stream<Arguments> provideInvalidCategoryNames() {
        return Stream.of(
                Arguments.of("Non-existent Category"),
                Arguments.of("Unknown"),
                Arguments.of("   ")
        );
    }

    private static Stream<Arguments> provideCategoriesForSave() {
        return Stream.of(
                Arguments.of(Category.builder().name("New Cat").build()),
                Arguments.of(Category.builder().name("Another New").description("Desc").build()),
                Arguments.of(Category.builder().name("123").description("456").build())
        );
    }

    // Tests
    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("provideCategoriesForFindAll")
    void findAll_VariousScenarios_ReturnsExpectedResult(List<Category> expected, String testName) {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(expected);

        // Act
        var result = categoryService.findAll();

        // Assert
        assertAll(
                () -> assertEquals(expected.size(), result.size(), "Size should match"),
                () -> assertEquals(expected, result, "Content should match"),
                () -> verify(categoryRepository).findAll()
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidCategoryNames")
    void findByName_ValidCategoryName_ReturnsCategory(String name) {
        // Arrange
        var category = Category.builder().name(name).build();
        when(categoryRepository.findByName(name)).thenReturn(Optional.of(category));

        // Act
        var result = categoryService.findByName(name);

        // Assert
        assertAll(
                () -> assertNotNull(result, "Category should not be null"),
                () -> assertEquals(name, result.getName(), "Name should match"),
                () -> verify(categoryRepository).findByName(name)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCategoryNames")
    void findByName_NonExistentCategory_ThrowsException(String name) {
        // Arrange
        when(categoryRepository.findByName(name)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.findByName(name));

        assertAll(
                () -> assertTrue(exception.getMessage().contains(name),
                        "Exception message should contain category name"),
                () -> verify(categoryRepository).findByName(name)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCategoriesForSave")
    void save_ValidCategory_ReturnsSavedCategory(Category category) {
        // Arrange
        when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        var result = categoryService.save(category);

        // Assert
        assertAll(
                () -> assertNotNull(result, "Saved category should not be null"),
                () -> assertEquals(category.getName(), result.getName(), "Names should match"),
                () -> verify(categoryRepository).existsByName(category.getName()),
                () -> verify(categoryRepository).save(category)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidCategoryNames")
    void save_DuplicateCategoryName_ThrowsException(String name) {
        // Arrange
        var category = Category.builder().name(name).build();
        when(categoryRepository.existsByName(name)).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(ResourceAlreadyExistException.class,
                () -> categoryService.save(category));

        assertAll(
                () -> assertTrue(exception.getMessage().contains(name),
                        "Exception message should contain category name"),
                () -> verify(categoryRepository).existsByName(name),
                () -> verify(categoryRepository, never()).save(any())
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, Updated Category, Updated Description",
            "2, New Name, New Desc",
            "3, Another Update, "
    })
    void update_ValidInput_ReturnsUpdatedCategory(String id, String newName, String newDesc) {
        // Arrange
        var existingCategory = Category.builder()
                .id(id)
                .name("Original")
                .description("Original Desc")
                .build();

        var updatedCategory = Category.builder()
                .name(newName)
                .description(newDesc)
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName(newName)).thenReturn(false);
        when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = categoryService.update(id, updatedCategory);

        // Assert
        assertAll(
                () -> assertEquals(id, result.getId(), "ID should remain the same"),
                () -> assertEquals(newName, result.getName(), "Name should be updated"),
                () -> assertEquals(newDesc, result.getDescription(), "Description should be updated"),
                () -> verify(categoryRepository).findById(id),
                () -> verify(categoryRepository).existsByName(newName),
                () -> verify(categoryRepository).save(existingCategory)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "abc"})
    void delete_ExistingCategory_DeletesSuccessfully(String id) {
        // Arrange
        var category = Category.builder().id(id).build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        // Act
        categoryService.delete(id);

        // Assert
        assertAll(
                () -> verify(categoryRepository).findById(id),
                () -> verify(categoryRepository).delete(category)
        );
    }
}