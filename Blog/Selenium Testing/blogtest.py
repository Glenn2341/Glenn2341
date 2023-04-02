from linktests import index
from smoketests import search
from selenium import webdriver

def main():  
    driver = webdriver.Chrome()

    results = index.runindextests(driver)
    results.extend(search.runsearchtests(driver))

    for result in results:
        print(result)

    
if __name__ == '__main__':
    main()
    