FROM python:3.11

COPY app/ app/
COPY requirements.txt requirements.txt

RUN chmod +x app/main.py
RUN pip unfreeze

CMD ["python", "app/main.py"]
