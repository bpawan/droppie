package com.droppie.api.model;

import com.droppie.api.annotation.TagConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaggedFile {

    @NotBlank(message = "File name must not be blank.")
    private String fileName;

    @TagConstraint(message = "The tags must be alphanumeric string in lower letters.")
    private List<String> tags;
}

