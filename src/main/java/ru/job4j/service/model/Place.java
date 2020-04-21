package ru.job4j.service.model;

import java.util.Objects;

public class Place implements Comparable<Place> {

    private Integer row;
    private Integer seat;
    private Boolean pick;
    private Integer idAccount;

    public Place() {
    }

    public Place(Integer row, Integer seat, Boolean pick, Integer idAccount) {
        this.row = row;
        this.seat = seat;
        this.pick = pick;
        this.idAccount = idAccount;
    }

    public Place(Integer row, Integer seat) {
        this(row, seat, false, null);
    }

    public Integer getRow() {
        return row;
    }

    public Integer getSeat() {
        return seat;
    }

    public Boolean getPick() {
        return pick;
    }

    public Integer getIdAccount() {
        return idAccount;
    }

    public void setPick(Boolean pick) {
        this.pick = pick;
    }

    public void setIdAccount(Integer idAccount) {
        this.idAccount = idAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place = (Place) o;
        return row.equals(place.row)
                && seat.equals(place.seat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, seat);
    }

    @Override
    public int compareTo(Place o) {
        int rowCompare = Integer.compare(this.row, o.getRow());
        if (rowCompare != 0) {
            return rowCompare;
        }
        return Integer.compare(this.seat, o.getSeat());
    }
}
