import unittest
from post_process import normalize_date, clean_name

class TestPostProcess(unittest.TestCase):

    def test_normalize_date_various_formats(self):
        self.assertEqual(normalize_date("2025-08-28"), ("2025-08-28", "2025-08-28"))
        self.assertEqual(normalize_date("28-08-2025"), ("2025-08-28", "2025-08-28"))
        self.assertEqual(normalize_date("28/08/2025"), ("2025-08-28", "2025-08-28"))
        self.assertEqual(normalize_date("6 agosto 2025"), ("2025-08-06", "2025-08-06"))
        self.assertEqual(normalize_date("28-29 agosto 2025"), ("2025-08-28", "2025-08-29"))
        self.assertIsNone(normalize_date("invalid date"))
        self.assertIsNone(normalize_date("N/A"))
        self.assertIsNone(normalize_date(""))

    def test_clean_name_removes_edition(self):
        self.assertEqual(clean_name("Sagra Della Porchetta 38ª edizione"), "Sagra Della Porchetta")
        self.assertEqual(clean_name("Sagra Della Porchetta 47ª"), "Sagra Della Porchetta")
        self.assertEqual(clean_name("Sagra Della Porchetta VII Edizione"), "Sagra Della Porchetta")
        self.assertEqual(clean_name("18ª Sagra delle Fregnacce e degli Arrosticini"), "Sagra Delle Fregnacce E Degli Arrosticini")

    def test_clean_name_removes_sagra_tag(self):
        self.assertEqual(clean_name("Sagra Della Porchetta (sagra)"), "Sagra Della Porchetta")

    def test_clean_name_handles_extra_spaces(self):
        self.assertEqual(clean_name("  Nome Evento  "), "Nome Evento")

    def test_clean_name_handles_trailing_chars(self):
        self.assertEqual(clean_name("Festa del vino - 47ª edizione"), "Festa Del Vino")
        self.assertEqual(clean_name("Madonna che scappa – Sulmona", "Sulmona"), "Madonna Che Scappa")


    def test_empty_and_na_name(self):
        self.assertEqual(clean_name(""), "")
        self.assertEqual(clean_name("n/a"), "")

if __name__ == '__main__':
    unittest.main()
