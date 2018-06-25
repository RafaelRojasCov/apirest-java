package App.Controllers;

import App.Models.Persona;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@RestController
public class PersonaController {
    private ArrayList<Persona> personas = new ArrayList<>();

    @RequestMapping(method=RequestMethod.GET, path = "/status", produces = "application/json")
    public Object home(){
        HashMap<String,String> obj = new HashMap<>();
        obj.put("message","La aplicacion ha sido cargada correctamente");
        return obj;
    }

    @RequestMapping(method=RequestMethod.GET, path = "/users", produces = "application/json")
    public Object listarPersonas(){
        return personas;
    }

    @RequestMapping(method=RequestMethod.GET, value="/user/{email}", produces = "application/json")
    public Object showUser(@PathVariable String email){
        HashMap<String,String> jsonObj = new HashMap<>();
        Iterator<Persona> it = personas.iterator();
        while(it.hasNext()){
            Persona p = it.next();
            if (p.getEmail().equals(email)){
                return p;
            }
        }
        jsonObj.put("message","La persona no fue encontrada");
        return jsonObj;
    }

    @RequestMapping(method=RequestMethod.POST, value="/new", produces = "application/json")
    public Object createUser(@RequestParam(value="firstName", defaultValue="") String firstName,
                             @RequestParam(value="lastName", defaultValue="") String lastName,
                             @RequestParam(value="email", defaultValue="") String email){
        Persona p = new Persona(email,firstName,lastName);
        HashMap<String,String> obj = new HashMap<>();

        if(p.getFirstName().equals("") || p.getLastName().equals("")){
            obj.put("message","Ha ocurrido un error al intentar registrar a la persona");
        } else {
            personas.add(p);
            obj.put("firstName", p.getFirstName());
            obj.put("lastName", p.getLastName());
            obj.put("email", p.getEmail());
        }
        return obj;
    }

    @RequestMapping(method=RequestMethod.PUT, value="/modify/{email}", produces="application/json")
    public Object modifyUser(@PathVariable("email") String email,
                             @RequestParam(value="firstName", defaultValue="") String firstName,
                             @RequestParam(value="lastName", defaultValue="") String lastName){
        HashMap<String,String> obj = new HashMap<>();
        Iterator<Persona> it = personas.iterator();

        while(it.hasNext()){
            Persona p = it.next();
            if(p.getEmail().equals(email)) {
                if (!firstName.equals("")) {
                    personas.get(personas.indexOf(p)).setFirstName(firstName);
                }
                if (!lastName.equals("")) {
                    personas.get(personas.indexOf(p)).setLastName(lastName);
                }
                obj.put("message", "La persona fue modificada con éxito");
                return obj;
            }
        }
        obj.put("message", "El correo no fue encontrado en la lista");
        return obj;
    }


    @RequestMapping(method=RequestMethod.DELETE, value="/delete/{email}", produces = "application/json")
    public Object deleteUser(@PathVariable String email){
        HashMap<String,String> obj = new HashMap<>();
        Iterator<Persona> it = personas.iterator();

        while(it.hasNext()){
            Persona p = it.next();
            if(p.getEmail().equals(email)) {
                personas.remove(p);
                obj.put("message", "La persona fue eliminada con éxito");
                return obj;
            }
        }
        obj.put("message", "El " + email + " no existe en la lista.");
        return obj;
    }

}
