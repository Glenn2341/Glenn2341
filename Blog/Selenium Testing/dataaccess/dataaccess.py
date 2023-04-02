import sqlite3

dbname = 'blog.db'

def get_post_titles_by_tag_id(tag_id):
    # Create a connection to the database
    conn = sqlite3.connect(dbname)
    c = conn.cursor()

    # Execute a query to find post ids associated with the given tag id
    c.execute("""
    SELECT postid FROM PostTag WHERE tagid=?
    """, (tag_id,))

    # Fetch all rows as a list of tuples
    post_ids = c.fetchall()

    # Execute a query to find post titles for the given post ids
    post_titles = []
    for post_id in post_ids:
        c.execute("""
        SELECT title FROM Post WHERE id=?
        """, (post_id[0],))
        title = c.fetchone()
        if title:
            post_titles.append(title[0])

    # Close the connection to the database
    conn.close()

    return post_titles


# Get tag(s) using id or all tags if no id is provided
def get_tags_by_id(tag_id=None):
    # Create a cursor
    c = sqlite3.connect(dbname).cursor()

    if tag_id:
        # Execute a query to select the row from the Tag table with the given tag_id
        c.execute("SELECT id, name FROM Tag WHERE id=?", (tag_id,))

        # Fetch the row as a tuple
        row = c.fetchone()

        # Return None if there's no row with the given tag_id
        if row is None:
            return None

        # Create a Tag object from the row and return it
        return ({'tagid': row[0], 'name': row[1]})
    else:
        # Execute a query to select all rows from the Tag table
        c.execute("SELECT id, name FROM Tag")

        # Fetch all rows as a list of tuples
        rows = c.fetchall()

        # Create Tag objects from the rows and return them as a list
        return [({'tagid': row[0], 'name': row[1]}) for row in rows]