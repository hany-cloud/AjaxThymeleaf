package net.hka.common.web.servlet.mvc.support;

import static net.hka.common.ui.Message.MESSAGE_ATTRIBUTE;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.hka.common.ui.Message;

/**
 * It is a utility helper class used to add a message attribute to the {@Link RedirectAttributes} or {@Link Model}.
 *
 */
public final class MessageHelper {

    private MessageHelper() {
    }

    /**
     * add success attribute message to the {@Link RedirectAttributes} object.
     * @param ra {@Link RedirectAttributes} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addSuccessAttribute(final RedirectAttributes ra, final String message, final Object... args) {
        addAttribute(ra, message, Message.Type.SUCCESS, args);
    }

    /**
     * add error attribute message to the {@Link RedirectAttributes} object.
     * @param ra {@Link RedirectAttributes} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addErrorAttribute(final RedirectAttributes ra, final String message, final Object... args) {
        addAttribute(ra, message, Message.Type.DANGER, args);
    }

    /**
     * add info attribute message to the {@Link RedirectAttributes} object.
     * @param ra {@Link RedirectAttributes} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addInfoAttribute(final RedirectAttributes ra, final String message, final Object... args) {
        addAttribute(ra, message, Message.Type.INFO, args);
    }

    /**
     * add warn attribute message to the {@Link RedirectAttributes} object.
     * @param ra {@Link RedirectAttributes} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addWarningAttribute(final RedirectAttributes ra, final String message, final Object... args) {
        addAttribute(ra, message, Message.Type.WARNING, args);
    }

    private static void addAttribute(RedirectAttributes ra, String message, Message.Type type, Object... args) {
    	
    	if(ra == null) throw new IllegalArgumentException("The redirectAttributes paremter is null");
    	if(message.isEmpty()) throw new IllegalArgumentException("The message paremter is empty");
    	
        ra.addFlashAttribute(MESSAGE_ATTRIBUTE, new Message(message, type, args));
    }

    /**
     * add success attribute message to the {@Link Model} object.
     * @param model {@Link Model} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addSuccessAttribute(final Model model, final String message, final Object... args) {
        addAttribute(model, message, Message.Type.SUCCESS, args);
    }

    /**
     * add error attribute message to the {@Link Model} object.
     * @param model {@Link Model} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addErrorAttribute(final Model model, final String message, final Object... args) {
        addAttribute(model, message, Message.Type.DANGER, args);
    }

    /**
     * add info attribute message to the {@Link Model} object.
     * @param model {@Link Model} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addInfoAttribute(final Model model, final String message, final Object... args) {
        addAttribute(model, message, Message.Type.INFO, args);
    }

    /**
     * add warn attribute message to the {@Link Model} object.
     * @param model {@Link Model} object
     * @param message the message to be added 
     * @param args @Nullable Object attributeValue
     */
    public static void addWarningAttribute(final Model model, final String message, final Object... args) {
        addAttribute(model, message, Message.Type.WARNING, args);
    }

    private static void addAttribute(Model model, String message, Message.Type type, Object... args) {
    	
    	if(model == null) throw new IllegalArgumentException("The model paremter is null");
    	if(message.isEmpty()) throw new IllegalArgumentException("The message paremter is empty");
    	
        model.addAttribute(MESSAGE_ATTRIBUTE, new Message(message, type, args));
    }
}
