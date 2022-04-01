package pl.skrys.validator;


import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.skrys.app.Project;
import pl.skrys.app.SpUserApp;
import pl.skrys.service.SpUserService;
import pl.skrys.service.SpUserServiceImpl;

public class SpUserValidator implements Validator {

    EmailValidator emailValidator = EmailValidator.getInstance();
    SpUserService userService;



    @Override
    public boolean supports(Class<?> clazz) {
        return SpUserApp.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object arg0, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "firstName", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "telephone", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.field.required");

        ValidationUtils.rejectIfEmpty(errors, "password", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "error.field.required");
        //ValidationUtils.rejectIfEmpty(errors, "age", "error.field.required");

        if((((SpUserApp)arg0).getPassword()).length()<6){
            errors.rejectValue("password", "error.size.pass");
        }

        if(errors.getErrorCount()==0){
            if(StringUtils.hasText(((SpUserApp)arg0).getEmail()) && emailValidator.isValid(((SpUserApp)arg0).getEmail())==false){
                errors.rejectValue("email", "error.email.invalid");
            }


            if(StringUtils.hasText(((SpUserApp)arg0).getPassword()) && StringUtils.hasText(((SpUserApp)arg0).getConfirmPassword())){
                if(!((SpUserApp)arg0).getPassword().equals(((SpUserApp)arg0).getConfirmPassword())){
                    errors.rejectValue("confirmPassword", "error.notMatch");
                }
            }


        }else{

            System.out.println("BLEDY Validator");
            System.out.println(errors.getAllErrors());
        }
    }

    public void validatePass(Object arg0, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "password", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "error.field.required");

        if((((SpUserApp)arg0).getPassword()).length()<6){
            errors.rejectValue("password", "error.size.pass");
        }

        if(errors.getErrorCount()==0){



            if(StringUtils.hasText(((SpUserApp)arg0).getPassword()) && StringUtils.hasText(((SpUserApp)arg0).getPassword())){
                if(!((SpUserApp)arg0).getPassword().equals(((SpUserApp)arg0).getConfirmPassword())){
                    errors.rejectValue("confirmPassword", "error.notMatch");
                }
            }




        }else{

            System.out.println("BLEDY Validator");
            System.out.println(errors.getAllErrors());
        }
    }


    public void validateWoPassword(Object arg0, Errors errors) {
        System.out.println(errors.getAllErrors());

        ValidationUtils.rejectIfEmpty(errors, "firstName", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "telephone", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.field.required");
        //ValidationUtils.rejectIfEmpty(errors, "age", "error.field.required");

        if(errors.getErrorCount()==0){
            if(StringUtils.hasText(((SpUserApp)arg0).getEmail()) && emailValidator.isValid(((SpUserApp)arg0).getEmail())==false){
                errors.rejectValue("email", "error.email.invalid");
            }
        }else{

            System.out.println("BLEDY Validator Wo pass");
            System.out.println(errors.getAllErrors());
        }
    }

    public void validateStation(Object arg0, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "nazwa", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "hala", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "sterownik", "error.field.required");
        //ValidationUtils.rejectIfEmpty(errors, "age", "error.field.required");

        if(errors.getErrorCount()==0){

        }else{

            System.out.println("BLEDY Validator");
            System.out.println(errors.getAllErrors());
        }
    }

    public void validateRobot(Object arg0, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "robotNumber", "error.field.required");

        if(errors.getErrorCount()==0){

        }else{

            System.out.println("BLEDY Validator");
            System.out.println(errors.getAllErrors());
        }
    }


    public void validateProject(Project project, BindingResult errors) {
        //TODO
        System.out.println("Validate Project");

        ValidationUtils.rejectIfEmpty(errors, "nazwa", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "country", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "city", "error.field.required");
        ValidationUtils.rejectIfEmpty(errors, "standard", "error.field.required");

        if(errors.getErrorCount()==0){

        }else{

            System.out.println("BLEDY Validator PROJEKT");
            System.out.println(errors.getAllErrors());
        }
    }
}
