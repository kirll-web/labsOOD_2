#include <iostream>
#include <memory>
#include <string>

// Product
class Pizza
{
private:
    std::string dough;
    std::string sauce;
    std::string topping;

public:
    Pizza() {}
    ~Pizza() {}

    void SetDough(const std::string& d) { dough = d; }
    void SetSauce(const std::string& s) { sauce = s; }
    void SetTopping(const std::string& t) { topping = t; }

    void ShowPizza()
    {
        std::cout << "Pizza with Dough as " << dough
            << ", Sauce as " << sauce
            << " and Topping as " << topping
            << " !!! " << std::endl;
    }
};

// Abstract Builder
class PizzaBuilder
{
protected:
    std::shared_ptr<Pizza> pizza;
public:
    PizzaBuilder() {}
    virtual ~PizzaBuilder() {}
    std::shared_ptr<Pizza> GetPizza() { return pizza; }

    void createNewPizzaProduct() { pizza.reset(new Pizza); }

    virtual void buildDough() = 0;
    virtual void buildSauce() = 0;
    virtual void buildTopping() = 0;

};

// ConcreteBuilder
class HawaiianPizzaBuilder : public PizzaBuilder
{
public:
    ~HawaiianPizzaBuilder() {}

    void buildDough() { pizza->SetDough("cross"); }
    void buildSauce() { pizza->SetSauce("mild"); }
    void buildTopping() { pizza->SetTopping("ham and pineapple"); }
};

// ConcreteBuilder
class SpicyPizzaBuilder : public PizzaBuilder
{
public:
    ~SpicyPizzaBuilder() {}

    void buildDough() { pizza->SetDough("pan baked"); }
    void buildSauce() { pizza->SetSauce("hot"); }
    void buildTopping() { pizza->SetTopping("pepperoni and salami"); }
};

// Director
class Waiter
{
public:
    Waiter() : pizzaBuilder(nullptr) {}
    ~Waiter() {}

    void SetPizzaBuilder(PizzaBuilder* b) { pizzaBuilder = b; }
    std::shared_ptr<Pizza> GetPizza() 
    {
        if (pizzaBuilder)
        {
            return pizzaBuilder->GetPizza();
        }
        else
        {
            std::cout << "Give me recept" << std::endl;
        }
    }

    void ConstructPizza()
    {
        if (pizzaBuilder)
        {
            pizzaBuilder->createNewPizzaProduct();
            pizzaBuilder->buildDough();
            pizzaBuilder->buildSauce();
            pizzaBuilder->buildTopping();
        }
        else
        {
            std::cout << "Give me recept" << std::endl;
        }

    }
private:
    PizzaBuilder* pizzaBuilder;
};

// Клиент заказывает две пиццы.
int main()
{
    Waiter waiter;

    waiter.ConstructPizza();
    std::shared_ptr<Pizza> pizza = waiter.GetPizza();
    HawaiianPizzaBuilder hawaiianPizzaBuilder;
    waiter.SetPizzaBuilder(&hawaiianPizzaBuilder);
    waiter.ConstructPizza();
    pizza = waiter.GetPizza();
    pizza->ShowPizza();

    SpicyPizzaBuilder spicyPizzaBuilder;
    waiter.SetPizzaBuilder(&spicyPizzaBuilder);
    waiter.ConstructPizza();
    pizza = waiter.GetPizza();
    pizza->ShowPizza();

    return EXIT_SUCCESS;
}