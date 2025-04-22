# Quality Challenge Mod

This mod saturates and pixelates your screen and lowers your fps depending on your health.
Optionally it also switches to a separate mic when low on health.

Made for [this YouTube video](https://youtu.be/fUr-W4xC3-E). Go watch it!!

Support me on [Ko-fi](https://ko-fi.com/quesia). ❤️❤️❤️

Join [this Discord server](https://discord.gg/s9m8gf6pju) for help! (After reading [the usage section](#usage))

# Usage

There's a config file at `.minecraft/config/value_config.json`. Here's what the values mean:

- `power` controls the formula the mod uses to determine how much deepfryness should be active at any moment in time.
- `offset` this value gets added on top of the player's max hp.
- `lowest_fps_val` controls how low your fps can go.
- `default_fps_val` controls what your default fps value is. (Should be your HZ preferably, because it just sets the fps based on this value and the minimum and then adjusts linearly based on the hp value)
- `deepfry_audio` toggle whether it should deepfry the audio.
- `grayscale death` toggle whether saturation should be set to 0 whenever the player is dead.
- `intensity_factor` the final factor to the saturation and pixelation.
- `health_percentage_until_earrape` controls what the percentage of your health should be before the mod starts distorting your audio

### Optional: OBS Setup

There are config values to integrate this mod with OBS websocket, however it is not required.
What this feature does is it switches your mic from your main one to a secondary one once your health falls below 5 hearts.

- `websocket_port` specifies the port your OBS websocket is running on.
- `websocket_password` specifies the password for the websocket. Leave empty if no password is configured.
- `good_mic_source_name` the name of your main microphone in OBS.
- `bad_mic_source_name` the name of your secondary microphone in OBS.

# Help

Join [this Discord server](https://discord.gg/s9m8gf6pju) for help!
