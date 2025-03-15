package tqs;

import jakarta.persistence.*;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String maker;
    private String model;
    private String segment;
    private String motorType;

    public Car() {}

    public Car(Long id, String maker, String model, String segment, String motorType) {
        this.id = id;
        this.maker = maker;
        this.model = model;
        this.segment = segment;
        this.motorType = motorType;
    }

    public Long getId() { return id; }
    public String getMaker() { return maker; }
    public String getModel() { return model; }
    public String getSegment() { return segment; }
    public String getMotorType() { return motorType; }

    public void setId(Long id) { this.id = id; }
    public void setMaker(String maker) { this.maker = maker; }
    public void setModel(String model) { this.model = model; }
    public void setSegment(String segment) { this.segment = segment; }
    public void setMotorType(String motorType) { this.motorType = motorType; }
}
