package fpt.swp.WorkSpace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "roomtype")
@Data
public class RoomType {

    @Id
    private String id;

    private String roomTypeName;

    @Column(name = "quantity")
    @ColumnDefault("0")
    private int quantity;

    @OneToMany(mappedBy = "roomType")
    @JsonIgnore
    private List<Room> rooms;







}
