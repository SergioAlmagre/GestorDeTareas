package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Usuario type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Usuarios", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Usuario implements Model {
  public static final QueryField ID = field("Usuario", "id");
  public static final QueryField ROL = field("Usuario", "rol");
  public static final QueryField NOMBRE_COMPLETO = field("Usuario", "nombreCompleto");
  public static final QueryField EMAIL = field("Usuario", "email");
  public static final QueryField TAREAS_FINALIZADAS = field("Usuario", "tareasFinalizadas");
  public static final QueryField FOTO_PERFIL = field("Usuario", "fotoPerfil");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int") Integer rol;
  private final @ModelField(targetType="String") String nombreCompleto;
  private final @ModelField(targetType="String") String email;
  private final @ModelField(targetType="Int") Integer tareasFinalizadas;
  private final @ModelField(targetType="String") String fotoPerfil;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public Integer getRol() {
      return rol;
  }
  
  public String getNombreCompleto() {
      return nombreCompleto;
  }
  
  public String getEmail() {
      return email;
  }
  
  public Integer getTareasFinalizadas() {
      return tareasFinalizadas;
  }
  
  public String getFotoPerfil() {
      return fotoPerfil;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Usuario(String id, Integer rol, String nombreCompleto, String email, Integer tareasFinalizadas, String fotoPerfil) {
    this.id = id;
    this.rol = rol;
    this.nombreCompleto = nombreCompleto;
    this.email = email;
    this.tareasFinalizadas = tareasFinalizadas;
    this.fotoPerfil = fotoPerfil;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Usuario usuario = (Usuario) obj;
      return ObjectsCompat.equals(getId(), usuario.getId()) &&
              ObjectsCompat.equals(getRol(), usuario.getRol()) &&
              ObjectsCompat.equals(getNombreCompleto(), usuario.getNombreCompleto()) &&
              ObjectsCompat.equals(getEmail(), usuario.getEmail()) &&
              ObjectsCompat.equals(getTareasFinalizadas(), usuario.getTareasFinalizadas()) &&
              ObjectsCompat.equals(getFotoPerfil(), usuario.getFotoPerfil()) &&
              ObjectsCompat.equals(getCreatedAt(), usuario.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), usuario.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRol())
      .append(getNombreCompleto())
      .append(getEmail())
      .append(getTareasFinalizadas())
      .append(getFotoPerfil())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Usuario {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("rol=" + String.valueOf(getRol()) + ", ")
      .append("nombreCompleto=" + String.valueOf(getNombreCompleto()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("tareasFinalizadas=" + String.valueOf(getTareasFinalizadas()) + ", ")
      .append("fotoPerfil=" + String.valueOf(getFotoPerfil()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Usuario justId(String id) {
    return new Usuario(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      rol,
      nombreCompleto,
      email,
      tareasFinalizadas,
      fotoPerfil);
  }
  public interface BuildStep {
    Usuario build();
    BuildStep id(String id);
    BuildStep rol(Integer rol);
    BuildStep nombreCompleto(String nombreCompleto);
    BuildStep email(String email);
    BuildStep tareasFinalizadas(Integer tareasFinalizadas);
    BuildStep fotoPerfil(String fotoPerfil);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private Integer rol;
    private String nombreCompleto;
    private String email;
    private Integer tareasFinalizadas;
    private String fotoPerfil;
    public Builder() {
      
    }
    
    private Builder(String id, Integer rol, String nombreCompleto, String email, Integer tareasFinalizadas, String fotoPerfil) {
      this.id = id;
      this.rol = rol;
      this.nombreCompleto = nombreCompleto;
      this.email = email;
      this.tareasFinalizadas = tareasFinalizadas;
      this.fotoPerfil = fotoPerfil;
    }
    
    @Override
     public Usuario build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Usuario(
          id,
          rol,
          nombreCompleto,
          email,
          tareasFinalizadas,
          fotoPerfil);
    }
    
    @Override
     public BuildStep rol(Integer rol) {
        this.rol = rol;
        return this;
    }
    
    @Override
     public BuildStep nombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep tareasFinalizadas(Integer tareasFinalizadas) {
        this.tareasFinalizadas = tareasFinalizadas;
        return this;
    }
    
    @Override
     public BuildStep fotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Integer rol, String nombreCompleto, String email, Integer tareasFinalizadas, String fotoPerfil) {
      super(id, rol, nombreCompleto, email, tareasFinalizadas, fotoPerfil);
      
    }
    
    @Override
     public CopyOfBuilder rol(Integer rol) {
      return (CopyOfBuilder) super.rol(rol);
    }
    
    @Override
     public CopyOfBuilder nombreCompleto(String nombreCompleto) {
      return (CopyOfBuilder) super.nombreCompleto(nombreCompleto);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder tareasFinalizadas(Integer tareasFinalizadas) {
      return (CopyOfBuilder) super.tareasFinalizadas(tareasFinalizadas);
    }
    
    @Override
     public CopyOfBuilder fotoPerfil(String fotoPerfil) {
      return (CopyOfBuilder) super.fotoPerfil(fotoPerfil);
    }
  }
  

  public static class UsuarioIdentifier extends ModelIdentifier<Usuario> {
    private static final long serialVersionUID = 1L;
    public UsuarioIdentifier(String id) {
      super(id);
    }
  }
  
}
