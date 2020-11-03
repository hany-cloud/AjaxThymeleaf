package net.hka.examples.thymeleaf.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class TaskDto implements Serializable, BaseDto {
    
	private Long id;

    @NotBlank(message = BaseDto.NOT_BLANK_MESSAGE)
    @Size(min = 2, message = BaseDto.MIN_SIZE_MESSAGE)
    private String title;

    @NotBlank(message = BaseDto.NOT_BLANK_MESSAGE)
    //Min(value = 2, message = "The entered value should be greater than {value} character(s)")
    @Size(min = 2, message = "The entered value should be greater than or equal to {min} character(s)")
    private String text;

    @DateTimeFormat(pattern = BaseDto.DATE_FORMAT)
    private String dueTo;
    
    
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getDueTo() {
        return dueTo;
    }

    public void setDueTo(final String dueTo) {
        this.dueTo = dueTo;
    }
       
	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", text=" + text + ", dueTo=" + dueTo
				+ "]";
	}
	
	
	@Override // Overriding equals for testing purposes and for best practice
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TaskDto task = (TaskDto) obj;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (text != null ? !text.equals(task.text) : task.text != null) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        return dueTo != null ? dueTo.equals(task.dueTo) : task.dueTo == null;
	}
}
