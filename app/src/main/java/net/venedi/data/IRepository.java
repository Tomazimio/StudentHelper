package net.venedi.data;


import java.util.List;

public interface IRepository<T>{

    public List<T> all();

    public T create(T object);

    public boolean update(T object);

    public T find(int id);

    public boolean delete(T object);

}
