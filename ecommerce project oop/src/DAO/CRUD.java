package DAO;

public interface CRUD<temp> {
    void create(temp entity);

    temp read(temp entity);

    void update(temp entity);

    void delete(temp entity);
}
