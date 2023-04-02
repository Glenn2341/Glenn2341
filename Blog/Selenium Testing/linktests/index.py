from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
import time
import re

entrypage = 'http://127.0.0.1:5000/'
searchpage = '/search'
homepage = '/index'
mailinglistpage = '/mailinglist'
aboutpage = '/about'
postspath = '/posts/'

#A series of link tests starting on the home page

def open_home_sidebar(driver):
    driver.get(entrypage)

    # Add a small delay to ensure the sidebar is visible before interacting with it
    time.sleep(0.5)

    # Find the button using an XPath that targets the anchor element within the 'sidebar' div and click it
    toggle_button = driver.find_element(By.XPATH, '//div[@id="sidebar"]//a[contains(@class, "toggle")]')
    toggle_button.click()

    # Add a small delay to ensure the sidebar elements are visible before interacting with it
    time.sleep(0.5)


def index_to_search(driver):
    open_home_sidebar(driver)

    # Find the search box and submit button elements using their IDs
    search_box = driver.find_element(By.ID, "query")
    submit_button = driver.find_element(By.ID, "search-btn")

    # Enter a search query in the search box
    search_box.send_keys("Yoga")

    # Click the submit button
    submit_button.click()

    #wait for results
    time.sleep(.5)

    # Get the current URL
    current_url = driver.current_url

    # Verify that the resulting page URL ends with "/search"
    try:
        assert current_url.endswith(searchpage), f"URL does not end with '{searchpage}': {current_url}"
        return True
    except Exception as e:
        return False


def index_to_sidebar_homepage(driver):
    open_home_sidebar(driver)

    # Find the 'Homepage' link inside the sidebar menu using XPath
    homepage_link = driver.find_element(By.XPATH, '//nav[@id="menu"]//ul//li//a[text()="Homepage"]')

    # Click the 'Homepage' link
    homepage_link.click()
    time.sleep(0.5)

    # Get the current URL
    current_url = driver.current_url

    # Verify that the resulting page URL ends with "/search"
    try:
        assert current_url.endswith(homepage), f"URL does not end with '{searchpage}': {current_url}"
        return True
    except Exception as e:
        return False
    

def index_to_sidebar_mailinglist(driver):
    open_home_sidebar(driver)

    # Find the link inside the sidebar menu using XPath
    link = driver.find_element(By.XPATH, '//nav[@id="menu"]//ul//li//a[text()="Mailing List"]')

    # Click the link
    link.click()
    time.sleep(0.5)

    # Get the current URL
    current_url = driver.current_url

    # Verify that the resulting page URL ends with "/search"
    try:
        assert current_url.endswith(mailinglistpage), f"URL does not end with '{mailinglistpage}': {current_url}"
        return True
    except Exception as e:
        return False


def index_to_sidebar_about(driver):
    open_home_sidebar(driver)

    # Find the link inside the sidebar menu using XPath
    link = driver.find_element(By.XPATH, '//nav[@id="menu"]//ul//li//a[text()="About us"]')

    # Click the link
    link.click()
    time.sleep(0.5)

    # Get the current URL
    current_url = driver.current_url

    # Verify that the resulting page URL ends with "/search"
    try:
        assert current_url.endswith(aboutpage), f"URL does not end with '{aboutpage}': {current_url}"
        return True
    except Exception as e:
        return False
    

#select the first post from the sidebar spotlight menu
def index_to_sidebar_spotlight(driver):
    open_home_sidebar(driver)
    # Find the first article within the section and then find the link inside it
    first_article = driver.find_element(By.XPATH, '//section//div[@class="mini-posts"]//article[1]//a')

    href = first_article.get_attribute('href')

    # Click the link in the first article
    first_article.click()

    # Wait for the page to load (optional)
    time.sleep(0.5)

    # Verify the correct page has loaded (optional)
    print(f"Loaded page: {driver.title}")

    # Extract the post_id using a regular expression
    post_id = re.search(r'/posts/(\d+)', href).group(1)

    # Store the post_id as an integer
    post_id = int(post_id)

    # Get the current URL
    current_url = driver.current_url

    try:
        assert(current_url.endswith(f'{postspath}{post_id}'))
        return True
    except Exception as e:
        return False
    

#select the first topic from the topic quickmenu
def index_to_topic_quickmenu(driver):
    driver.get(entrypage)

    # Find the first topic submit button within the section
    first_topic_button = driver.find_element(By.XPATH, '//article[contains(@class, "half-width")]//form//button')

    # Click the first topic button
    first_topic_button.click()

    # Wait for the page to load (optional)
    time.sleep(0.5)

    # Get the current URL
    current_url = driver.current_url

    # Verify that the resulting page URL ends with "/search"
    try:
        assert current_url.endswith(searchpage), f"URL does not end with '{searchpage}': {current_url}"
        return True
    except Exception as e:
        return False
    

#select the first post from the main postlist
def index_to_mainpage_post(driver):
    driver.get(entrypage)

    # Find the first article within the section and then find the read button inside it
    first_image_link = driver.find_element(By.XPATH, '//section//div[@class="posts"]//article[1]//a[@class="image"]')

    # Get the href attribute of the read button
    href = first_image_link.get_attribute('href')

    # Extract the post_id using a regular expression
    post_id = re.search(r'/posts/(\d+)', href).group(1)

    # Store the post_id as an integer
    post_id = int(post_id)

    # Click the read button in the first article
    first_image_link.click()

    # Wait for the page to load (optional)
    time.sleep(0.5)

    # Get the current URL
    current_url = driver.current_url

    try:
        assert(current_url.endswith(f'{postspath}{post_id}'))
        return True
    except Exception as e:
        return False


def runindextests(driver):

    results = []
    results.append({index_to_search.__name__: index_to_search(driver)}) 
    results.append({index_to_sidebar_homepage.__name__: index_to_sidebar_homepage(driver)})
    results.append({index_to_sidebar_mailinglist.__name__: index_to_sidebar_mailinglist(driver)})
    results.append({index_to_sidebar_about.__name__: index_to_sidebar_about(driver)})
    results.append({index_to_sidebar_spotlight.__name__: index_to_sidebar_spotlight(driver)})
    results.append({index_to_topic_quickmenu.__name__: index_to_topic_quickmenu(driver)})
    results.append({index_to_mainpage_post.__name__: index_to_mainpage_post(driver)})

    #print(results)
    return(results)


#runindextests()
