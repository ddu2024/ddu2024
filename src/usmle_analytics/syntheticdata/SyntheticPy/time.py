from datetime import datetime, timedelta

# Get current time in UTC
current_utc_time = datetime.utcnow()

# Define the time difference for Eastern Time (ET) from UTC
et_time_difference = timedelta(hours=-5)  # Eastern Time is UTC-5

# Convert current UTC time to Eastern Time (ET)
current_et_time = current_utc_time + et_time_difference

# Subtract 3 hours in ET
new_et_time = current_et_time - timedelta(hours=3)

# Print the results
print("Current UTC time:", current_utc_time)
print("Current ET time:", current_et_time)
print("New time (ET - 3 hours):", new_et_time)


wo_hours = (datetime.now() - timedelta(hours = 6)).strftime("%Y-%m-%d %H:%M:%S")
print(wo_hours)