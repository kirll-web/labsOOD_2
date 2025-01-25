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
    Waiter() : mPizzaBuilder(nullptr) {}
    ~Waiter() {}

    void SetPizzaBuilder(PizzaBuilder* b) { mPizzaBuilder = b; }
    std::shared_ptr<Pizza> GetPizza() 
    {
        if (mPizzaBuilder)
        {
            return mPizzaBuilder->GetPizza();
        }
        else
        {
            std::cout << "Give me recept" << std::endl;
        }
    }

    void ConstructPizza()
    {
        if (mPizzaBuilder)
        {
            mPizzaBuilder->createNewPizzaProduct();
            mPizzaBuilder->buildDough();
            mPizzaBuilder->buildSauce();
            mPizzaBuilder->buildTopping();
        }
        else
        {
            std::cout << "Give me recept" << std::endl;
        }

    }
private:
    PizzaBuilder* mPizzaBuilder;
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