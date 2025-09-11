package it.sagratime.app.core.components

import androidx.compose.runtime.Composable
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.abruzzo
import it.sagratime.app_core.generated.resources.basilicata
import it.sagratime.app_core.generated.resources.calabria
import it.sagratime.app_core.generated.resources.campania
import it.sagratime.app_core.generated.resources.emilia_romagna
import it.sagratime.app_core.generated.resources.friuli_venezia_giulia
import it.sagratime.app_core.generated.resources.lazio
import it.sagratime.app_core.generated.resources.liguria
import it.sagratime.app_core.generated.resources.lombardia
import it.sagratime.app_core.generated.resources.marche
import it.sagratime.app_core.generated.resources.molise
import it.sagratime.app_core.generated.resources.piemonte
import it.sagratime.app_core.generated.resources.puglia
import it.sagratime.app_core.generated.resources.sardegna
import it.sagratime.app_core.generated.resources.sicilia
import it.sagratime.app_core.generated.resources.toscana
import it.sagratime.app_core.generated.resources.trentino_alto_adige
import it.sagratime.app_core.generated.resources.umbria
import it.sagratime.app_core.generated.resources.valle_d_aosta
import it.sagratime.app_core.generated.resources.veneto
import it.sagratime.core.data.ItalianRegion
import org.jetbrains.compose.resources.stringResource

@Composable
fun ItalianRegion.localizedString(): String {
    val res =
        when (this) {
            ItalianRegion.Abruzzo -> Res.string.abruzzo
            ItalianRegion.Basilicata -> Res.string.basilicata
            ItalianRegion.Calabria -> Res.string.calabria
            ItalianRegion.Campania -> Res.string.campania
            ItalianRegion.EmiliaRomagna -> Res.string.emilia_romagna
            ItalianRegion.FriuliVeneziaGiulia -> Res.string.friuli_venezia_giulia
            ItalianRegion.Lazio -> Res.string.lazio
            ItalianRegion.Liguria -> Res.string.liguria
            ItalianRegion.Lombardia -> Res.string.lombardia
            ItalianRegion.Marche -> Res.string.marche
            ItalianRegion.Molise -> Res.string.molise
            ItalianRegion.Piemonte -> Res.string.piemonte
            ItalianRegion.Puglia -> Res.string.puglia
            ItalianRegion.Sardegna -> Res.string.sardegna
            ItalianRegion.Sicilia -> Res.string.sicilia
            ItalianRegion.Toscana -> Res.string.toscana
            ItalianRegion.TrentinoAltoAdige -> Res.string.trentino_alto_adige
            ItalianRegion.Umbria -> Res.string.umbria
            ItalianRegion.ValledAosta -> Res.string.valle_d_aosta
            ItalianRegion.Veneto -> Res.string.veneto
        }
    return stringResource(res)
}
