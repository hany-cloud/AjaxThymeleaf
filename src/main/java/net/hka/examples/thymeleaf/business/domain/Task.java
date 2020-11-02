package net.hka.examples.thymeleaf.business.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Task {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @Column(name = "due_to")
    private Date dueTo;
    
    //@Version
    private Calendar created = Calendar.getInstance();
    
    public Task() {
    }

    public Task(final String title, final String text, final Date dueTo) {
    	
    	this.title = title;
        this.text = text;
        this.dueTo = dueTo;
    }
    
    public Task(final Long id, final String title, final String text, final Date dueTo) {
    	
    	this(title, text, dueTo);
    	this.id = id;
    }
    
    
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

    public Date getDueTo() {
        return dueTo;
    }

    public void setDueTo(final Date dueTo) {
        this.dueTo = dueTo;
    }
    
    public Calendar getCreated() {
        return created;
    }
    
    
	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", text=" + text + ", created=" + created + ", dueTo=" + dueTo
				+ "]";
	}

	// Overriding equals for testing purposes
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task task = (Task) obj;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (text != null ? !text.equals(task.text) : task.text != null) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        return dueTo != null ? dueTo.equals(task.dueTo) : task.dueTo == null;
		
	}
    
}
