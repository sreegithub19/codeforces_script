exec('''sudo apt-get install -y maven
mvn dependency:copy-dependencies -DoutputDirectory=libs
python -m pip install --upgrade pip
pip install jupyter nbconvert
python test.py
cd execute
jupyter nbconvert --to notebook --execute notebook.ipynb --output executed_notebook.ipynb
cd .. 
cd execute
jupyter nbconvert --to html executed_notebook.ipynb --output executed_notebook
cd .. 
ls -la
''')