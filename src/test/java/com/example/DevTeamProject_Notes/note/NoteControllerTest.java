package com.example.DevTeamProject_Notes.note;

import com.example.DevTeamProject_Notes.security.WithMockCustomUser;
import com.example.DevTeamProject_Notes.user.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class NoteControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @MockBean
    private NoteService noteService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void noteList() {
        when(noteService.findPaginated(1, 5, 100L))
                .thenReturn(new PageImpl<>(List.of(getNote(100L))));

        mvc.perform(get("/note/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("note/note-list"))
                .andExpect(model().attribute("notes", hasSize(1)))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("title", is("Test Note")),
                                hasProperty("content", is("Test Note Content")),
                                hasProperty("privacy", is(Privacy.PUBLIC))
                        )
                )));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void findPaginated() {
        PageImpl<Note> page = new PageImpl<>(List.of(getNote(100L)));
        when(noteService.findPaginated(3, 5, 100L))
                .thenReturn(page);

        mvc.perform(get("/note/list/page/3"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/note-list"))
                .andExpect(model().attribute("page", is(page)))
                .andExpect(model().attribute("currentPage", is(3)))
                .andExpect(model().attribute("totalPages", is(page.getTotalPages())))
                .andExpect(model().attribute("totalItems", is(page.getTotalElements())))
                .andExpect(model().attribute("notes", hasSize(1)));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void createNote() {
        mvc.perform(get("/note/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/note-form"))
                .andExpect(model().attribute("note", notNullValue()));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void testCreateNote() {
        mvc.perform(post("/note/save")
                        .param("privacy", "PUBLIC")
                        .flashAttr("note", new Note()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/note/list"));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void testCreateNoteWithError() {
        Note value = new Note();
        value.setTitle("1");
        mvc.perform(post("/note/save")
                        .param("privacy", "PUBLIC")
                        .flashAttr("note", value))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("note/note-form"));
    }

    @Test
    void editNote() {
    }

    @Test
    void deleteNote() {
    }

    @Test
    void shareNote() {
    }

    @Test
    void getLink() {
    }

    private static Note getNote(long userId) {
        Note note = new Note();
        note.setTitle("Test Note");
        note.setContent("Test Note Content");
        note.setPrivacy(Privacy.PUBLIC);
        User user = new User();
        user.setId(userId);
        note.setUser(user);
        return note;
    }
}