package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasOne;
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

/** This is an auto generated class representing the Tarea type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tareas", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Tarea implements Model {
  public static final QueryField ID = field("Tarea", "id");
  public static final QueryField DESCRIPCION = field("Tarea", "descripcion");
  public static final QueryField DIFICULTAD = field("Tarea", "dificultad");
  public static final QueryField ESTIMACION_HORAS = field("Tarea", "estimacionHoras");
  public static final QueryField HORAS_INVERTIDAS = field("Tarea", "horasInvertidas");
  public static final QueryField ESTA_ASIGNADA = field("Tarea", "estaAsignada");
  public static final QueryField ESTA_FINALIZADA = field("Tarea", "estaFinalizada");
  public static final QueryField TAREA_USUARIO_TAREA_ID = field("Tarea", "tareaUsuarioTareaId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String descripcion;
  private final @ModelField(targetType="String") String dificultad;
  private final @ModelField(targetType="Float") Float estimacionHoras;
  private final @ModelField(targetType="Float") Float horasInvertidas;
  private final @ModelField(targetType="Boolean") Boolean estaAsignada;
  private final @ModelField(targetType="Boolean") Boolean estaFinalizada;
  private final @ModelField(targetType="Usuario") @HasOne(associatedWith = "id", type = Usuario.class) Usuario UsuarioTarea = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String tareaUsuarioTareaId;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getDescripcion() {
      return descripcion;
  }
  
  public String getDificultad() {
      return dificultad;
  }
  
  public Float getEstimacionHoras() {
      return estimacionHoras;
  }
  
  public Float getHorasInvertidas() {
      return horasInvertidas;
  }
  
  public Boolean getEstaAsignada() {
      return estaAsignada;
  }
  
  public Boolean getEstaFinalizada() {
      return estaFinalizada;
  }
  
  public Usuario getUsuarioTarea() {
      return UsuarioTarea;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getTareaUsuarioTareaId() {
      return tareaUsuarioTareaId;
  }
  
  private Tarea(String id, String descripcion, String dificultad, Float estimacionHoras, Float horasInvertidas, Boolean estaAsignada, Boolean estaFinalizada, String tareaUsuarioTareaId) {
    this.id = id;
    this.descripcion = descripcion;
    this.dificultad = dificultad;
    this.estimacionHoras = estimacionHoras;
    this.horasInvertidas = horasInvertidas;
    this.estaAsignada = estaAsignada;
    this.estaFinalizada = estaFinalizada;
    this.tareaUsuarioTareaId = tareaUsuarioTareaId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Tarea tarea = (Tarea) obj;
      return ObjectsCompat.equals(getId(), tarea.getId()) &&
              ObjectsCompat.equals(getDescripcion(), tarea.getDescripcion()) &&
              ObjectsCompat.equals(getDificultad(), tarea.getDificultad()) &&
              ObjectsCompat.equals(getEstimacionHoras(), tarea.getEstimacionHoras()) &&
              ObjectsCompat.equals(getHorasInvertidas(), tarea.getHorasInvertidas()) &&
              ObjectsCompat.equals(getEstaAsignada(), tarea.getEstaAsignada()) &&
              ObjectsCompat.equals(getEstaFinalizada(), tarea.getEstaFinalizada()) &&
              ObjectsCompat.equals(getCreatedAt(), tarea.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), tarea.getUpdatedAt()) &&
              ObjectsCompat.equals(getTareaUsuarioTareaId(), tarea.getTareaUsuarioTareaId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDescripcion())
      .append(getDificultad())
      .append(getEstimacionHoras())
      .append(getHorasInvertidas())
      .append(getEstaAsignada())
      .append(getEstaFinalizada())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getTareaUsuarioTareaId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Tarea {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("descripcion=" + String.valueOf(getDescripcion()) + ", ")
      .append("dificultad=" + String.valueOf(getDificultad()) + ", ")
      .append("estimacionHoras=" + String.valueOf(getEstimacionHoras()) + ", ")
      .append("horasInvertidas=" + String.valueOf(getHorasInvertidas()) + ", ")
      .append("estaAsignada=" + String.valueOf(getEstaAsignada()) + ", ")
      .append("estaFinalizada=" + String.valueOf(getEstaFinalizada()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("tareaUsuarioTareaId=" + String.valueOf(getTareaUsuarioTareaId()))
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
  public static Tarea justId(String id) {
    return new Tarea(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      descripcion,
      dificultad,
      estimacionHoras,
      horasInvertidas,
      estaAsignada,
      estaFinalizada,
      tareaUsuarioTareaId);
  }
  public interface BuildStep {
    Tarea build();
    BuildStep id(String id);
    BuildStep descripcion(String descripcion);
    BuildStep dificultad(String dificultad);
    BuildStep estimacionHoras(Float estimacionHoras);
    BuildStep horasInvertidas(Float horasInvertidas);
    BuildStep estaAsignada(Boolean estaAsignada);
    BuildStep estaFinalizada(Boolean estaFinalizada);
    BuildStep tareaUsuarioTareaId(String tareaUsuarioTareaId);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String descripcion;
    private String dificultad;
    private Float estimacionHoras;
    private Float horasInvertidas;
    private Boolean estaAsignada;
    private Boolean estaFinalizada;
    private String tareaUsuarioTareaId;
    public Builder() {
      
    }
    
    private Builder(String id, String descripcion, String dificultad, Float estimacionHoras, Float horasInvertidas, Boolean estaAsignada, Boolean estaFinalizada, String tareaUsuarioTareaId) {
      this.id = id;
      this.descripcion = descripcion;
      this.dificultad = dificultad;
      this.estimacionHoras = estimacionHoras;
      this.horasInvertidas = horasInvertidas;
      this.estaAsignada = estaAsignada;
      this.estaFinalizada = estaFinalizada;
      this.tareaUsuarioTareaId = tareaUsuarioTareaId;
    }
    
    @Override
     public Tarea build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Tarea(
          id,
          descripcion,
          dificultad,
          estimacionHoras,
          horasInvertidas,
          estaAsignada,
          estaFinalizada,
          tareaUsuarioTareaId);
    }
    
    @Override
     public BuildStep descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }
    
    @Override
     public BuildStep dificultad(String dificultad) {
        this.dificultad = dificultad;
        return this;
    }
    
    @Override
     public BuildStep estimacionHoras(Float estimacionHoras) {
        this.estimacionHoras = estimacionHoras;
        return this;
    }
    
    @Override
     public BuildStep horasInvertidas(Float horasInvertidas) {
        this.horasInvertidas = horasInvertidas;
        return this;
    }
    
    @Override
     public BuildStep estaAsignada(Boolean estaAsignada) {
        this.estaAsignada = estaAsignada;
        return this;
    }
    
    @Override
     public BuildStep estaFinalizada(Boolean estaFinalizada) {
        this.estaFinalizada = estaFinalizada;
        return this;
    }
    
    @Override
     public BuildStep tareaUsuarioTareaId(String tareaUsuarioTareaId) {
        this.tareaUsuarioTareaId = tareaUsuarioTareaId;
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
    private CopyOfBuilder(String id, String descripcion, String dificultad, Float estimacionHoras, Float horasInvertidas, Boolean estaAsignada, Boolean estaFinalizada, String tareaUsuarioTareaId) {
      super(id, descripcion, dificultad, estimacionHoras, horasInvertidas, estaAsignada, estaFinalizada, tareaUsuarioTareaId);
      
    }
    
    @Override
     public CopyOfBuilder descripcion(String descripcion) {
      return (CopyOfBuilder) super.descripcion(descripcion);
    }
    
    @Override
     public CopyOfBuilder dificultad(String dificultad) {
      return (CopyOfBuilder) super.dificultad(dificultad);
    }
    
    @Override
     public CopyOfBuilder estimacionHoras(Float estimacionHoras) {
      return (CopyOfBuilder) super.estimacionHoras(estimacionHoras);
    }
    
    @Override
     public CopyOfBuilder horasInvertidas(Float horasInvertidas) {
      return (CopyOfBuilder) super.horasInvertidas(horasInvertidas);
    }
    
    @Override
     public CopyOfBuilder estaAsignada(Boolean estaAsignada) {
      return (CopyOfBuilder) super.estaAsignada(estaAsignada);
    }
    
    @Override
     public CopyOfBuilder estaFinalizada(Boolean estaFinalizada) {
      return (CopyOfBuilder) super.estaFinalizada(estaFinalizada);
    }
    
    @Override
     public CopyOfBuilder tareaUsuarioTareaId(String tareaUsuarioTareaId) {
      return (CopyOfBuilder) super.tareaUsuarioTareaId(tareaUsuarioTareaId);
    }
  }
  

  public static class TareaIdentifier extends ModelIdentifier<Tarea> {
    private static final long serialVersionUID = 1L;
    public TareaIdentifier(String id) {
      super(id);
    }
  }
  
}
