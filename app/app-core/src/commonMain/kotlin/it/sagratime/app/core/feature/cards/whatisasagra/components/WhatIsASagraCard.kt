package it.sagratime.app.core.feature.cards.whatisasagra.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.RoundedIcon
import it.sagratime.app.core.components.SagraTimeCard
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.what_is_a_sagra_community_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_community_snippet_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_culture_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_culture_snippet_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_land_identity_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_land_identity_snippet_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_subtitle
import it.sagratime.app_core.generated.resources.what_is_a_sagra_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_tradition_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_tradition_snippet_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun WhatIsASagraCard(modifier: Modifier = Modifier) {
    SagraTimeCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(SagraTimeTheme.metrics.cards.innerPaddings),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundedIcon {
                Icon(
                    imageVector = Icons.Outlined.QuestionMark,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
            Text(
                text = stringResource(Res.string.what_is_a_sagra_title),
                style = SagraTimeTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(Res.string.what_is_a_sagra_subtitle),
                style = SagraTimeTheme.typography.bodyLarge,
            )
            InfoSnippet(
                title = stringResource(Res.string.what_is_a_sagra_tradition_snippet_title),
                description = stringResource(Res.string.what_is_a_sagra_tradition_snippet_description),
                icon = {
                    RoundedIcon {
                        Icon(
                            imageVector = Icons.Filled.FoodBank,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
            InfoSnippet(
                title = stringResource(Res.string.what_is_a_sagra_community_snippet_title),
                description = stringResource(Res.string.what_is_a_sagra_community_snippet_description),
                icon = {
                    RoundedIcon {
                        Icon(
                            imageVector = Icons.Filled.People,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
            InfoSnippet(
                title = stringResource(Res.string.what_is_a_sagra_land_identity_snippet_title),
                description = stringResource(Res.string.what_is_a_sagra_land_identity_snippet_description),
                icon = {
                    RoundedIcon {
                        Icon(
                            imageVector = Icons.Filled.Map,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
            InfoSnippet(
                title = stringResource(Res.string.what_is_a_sagra_culture_snippet_title),
                description = stringResource(Res.string.what_is_a_sagra_culture_snippet_description),
                icon = {
                    RoundedIcon {
                        Icon(
                            imageVector = Icons.Filled.MusicNote,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        }
    }
}
