#include <iostream>
#include <cassert>

using namespace std;

template<typename T>
class LinkedList {
private:
    struct Node {
        T value;
        Node *next;

        Node(T value, Node *next) {
            this->value = value;
            this->next = next;
        };
    };

    int size = 0;
    Node *first;
    Node *last;
public:

    int getSize() {
        return size;
    }

    bool isEmpty(){
        return size == 0;
    }

    void addFirst(T value) {
        if (size > 0) {
            first = new Node(value, first);
        } else {
            first = new Node(value, nullptr);
            last = first;
        }
        size++;
    }

    void addLast(T value) {
        if (size > 0) {
            last->next = new Node(value, nullptr);
            last = last->next;
        } else {
            first = new Node(value, nullptr);
            last = first;
        }
        size++;
    }


    void add(T value, int ind) {
        assert(ind <= size && ind > -1);
        if (ind == 0) {
            addFirst(value);
            return;
        }
        if (ind == size) {
            addLast(value);
            return;
        }
        Node *req = first;
        for (int i = 0; i < ind - 1; i++) {
            req = req->next;
        }
        Node *temp = new Node(value, req->next);
        req->next = temp;
        size++;

    }

    void set(T value, int ind) {
        assert(ind < size && ind > -1);
        Node *req = first;
        for (int i = 0; i < ind; i++) {
            req = req->next;
        }
        req->value = value;
    }

    T get(int ind) {
        assert(ind < size && ind > -1);
        Node *req = first;
        for (int i = 0; i < ind; i++) {
            req = req->next;
        }
        return req->value;
    }

    LinkedList<T> copy() {
        LinkedList<T> b;
        Node *req = first;
        for (int i = 0; i < size; i++) {
            b.addLast(req->value);
            req = req->next;
        }
        return b;
    }

    void clear() {
        while (size > 0) {
            Node *second = first->next;
            delete first;
            first = second;
            size--;
        }
    }

    T *toArray() {
        T *a = new T[getSize()];
        Node *req = first;
        for (int i = 0; i < size; i++) {
            a[i] = (req->value);
            req = req->next;
        }
        return a;
    }

    void remove(int ind) {
        assert(ind < size && ind > -1);
        if (ind == 0) {
            Node *tmp = first;
            first = first->next;
            delete tmp;
        }
        if (ind > 0) {
            Node *req = first;
            for (int i = 0; i < ind - 1; i++) {
                req = req->next;
            }
            if (ind == size - 1) {
                last = req;
                delete req->next;
            } else {
                Node *tmp = req->next;
                req->next = req->next->next;
                delete tmp;
            }
        }
        size--;
    }

    void swap(int ind1, int ind2){
        T obj1 = get(ind1);
        T obj2 = get(ind2);
        set(obj1, ind2);
        set(obj2, ind1);
    }
};


int main() {
    LinkedList<int> list;
    list.addFirst(1);
    list.addFirst(2);
    list.addLast(3);
    list.add(4, 1);
    cout << list.get(2) << '\n';
    list.add(10, 2);
    list.add(20, 2);
    cout << list.getSize() << '\n';
    list.set(30, list.getSize()-1);
    list.swap(1, list.getSize()-2);
    cout << list.get(list.getSize()-2) << '\n';
    list.clear();
    cout << list.isEmpty();
}