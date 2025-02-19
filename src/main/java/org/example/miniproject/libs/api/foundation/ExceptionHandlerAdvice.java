package org.example.miniproject.libs.api.foundation;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.miniproject.libs.api.response.ApiErrorResponse;
import org.example.miniproject.libs.exceptions.NotFoundException;
import org.example.miniproject.libs.exceptions.ServiceException;
import org.example.miniproject.libs.exceptions.ValidateException;
import org.example.miniproject.libs.services.MessageService;
import org.example.miniproject.libs.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@Slf4j
//@RestControllerAdvice
public class ExceptionHandlerAdvice
{
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * the {@linkplain MessageService}
     */
    @Resource
    private MessageService messageService;

    /**
     * handle validated exception do by the annotation valid.
     *
     * @param ex      the exception
     * @param request the current request
     * @return {@code ApiErrorResponse}
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class,
            ValidateException.class })
    public ResponseEntity<ApiErrorResponse> handleValidatedException(Exception ex,
                                                                     WebRequest request) {

        // get {@code ObjectError} from ValidatedException
        List<ObjectError> objectErrors = new ArrayList<>();
        if (ex instanceof BindException) {
            objectErrors = ((BindException) ex).getAllErrors();
        } else if (ex instanceof MethodArgumentNotValidException) {
            objectErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
        } else if (ex instanceof ValidateException) {
            objectErrors = ((ValidateException) ex).getBindingResult().getAllErrors();
        }

        ApiErrorResponse res = createResponse(ApiErrorResponse.ERROR_VALIDATION,
                messageService.convertObjectErrors(objectErrors));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()).body(res);
    }

    /**
     * handle service exception.
     *
     * @param ex      the exception
     * @param request the current request
     * @return {@code ApiErrorResponse}
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceException(ServiceException ex,
                                                                   WebRequest request) {

        List<Message> messages = ex.getMessages();
        messageService.resolveMessageBody(messages);

        ApiErrorResponse res = createResponse(ApiErrorResponse.ERROR_SERVICE, messages);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()).body(res);
    }

    /**
     * handle Resource Not Found Exception.
     *
     * @param ex      the exception
     * @param request the current request
     * @return {@code ApiErrorResponse}
     */
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(Exception ex,
                                                                            WebRequest request) {

        ApiErrorResponse res = createResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()).body(res);
    }

    /**
     * create error response.
     *
     * @param error    the error title
     * @param messages the message list
     * @return
     */
    private ApiErrorResponse createResponse(String error, List<Message> messages) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setError(error);
        response.setMessages(messages);

        // output log
        if (logger.isInfoEnabled()) {
            logger.info("A {} error occurred. response={}", error,
                    JsonUtils.serializeQuietly(response));
        }

        return response;
    }
}
