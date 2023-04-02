from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
import time
from dataaccess import dataaccess
import re

entrypage = 'http://127.0.0.1:5000/'
searchpage = entrypage + '/search'


#Search for a given topictag  and check if the results are correct
def test_topic_search(tag, driver):
    driver.get(searchpage)

    tagname = tag['name']
    tagid = int(tag['tagid'])

    #Get the titles of posts that should appear
    expected_post_titles = dataaccess.get_post_titles_by_tag_id(tagid)

    # Find the search input element using its ID
    search_input = driver.find_element(By.ID, "query")

    #Enter the search text in the search input
    search_text = tagname
    search_input.send_keys(search_text)

    # Find the submit button using its ID
    submit_button = driver.find_element(By.ID, "search-btn")

    # Click the submit button
    submit_button.click()

    # Wait for the results (optional)
    time.sleep(0.5)

    # Find all the post title elements
    post_title_elements = driver.find_elements(By.XPATH, '//article/header/h3')

    # Get all the post titles
    post_titles = [title_element.text for title_element in post_title_elements]

    #Verify the expected posts are present
    for post in expected_post_titles:
        try:
            assert(post in post_titles)
        except Exception as e:
            return False

    return True


def runsearchtests(driver):
    driver = webdriver.Chrome()

    all_tags = dataaccess.get_tags_by_id()

    first_tag = all_tags[0]
    middle_tag = all_tags[int(len(all_tags) / 2)]

    results = []
    results.append({test_topic_search.__name__: test_topic_search(first_tag, driver)}) 
    results.append({test_topic_search.__name__: test_topic_search(middle_tag, driver)}) 

    #print(results)
    return(results)

#runsearchtests()